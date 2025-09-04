package com.wx2.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.api.client.ProductFeignClient;
import com.wx2.api.client.UserFeignClient;
import com.wx2.common.error.ProductError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.dto.OrderDelayMessage;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.common.model.vo.CouponVO;
import com.wx2.common.model.vo.ProductVO;
import com.wx2.common.utils.UserContext;
import com.wx2.order.enums.OrderStatus;
import com.wx2.order.mapper.OrderMapper;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.order.model.entity.Order;
import com.wx2.order.model.entity.OrderDetail;
import com.wx2.order.model.query.OrderQuery;
import com.wx2.order.service.OrderDetailService;
import com.wx2.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.wx2.common.constant.MqConstant.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final UserFeignClient userFeignClient;
    private final ProductFeignClient productFeignClient;
    private final OrderDetailService orderDetailService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @GlobalTransactional
    public Long addOrder(OrderQuery query) {
        // 初始化订单基本信息
        Order order = new Order();
        Long userId = UserContext.getUserId();
        BeanUtil.copyProperties(query, order);
        order.setUserId(userId);
        // 处理订单详情
        List<OrderDetailDTO> orderDetailDTOList = query.getDetailList();
        Map<Long, Integer> productNumMap = orderDetailDTOList.stream()
                .collect(Collectors.toMap(OrderDetailDTO::getProductId, OrderDetailDTO::getNum));

        Set<Long> productIdSet = productNumMap.keySet();
        List<Long> productIdList = new ArrayList<>(productIdSet);
        // 调用商品服务查询商品信息
        List<ProductVO> productList = productFeignClient.getProductByIds(productIdList);
        // 检验商品是否存在
        if (ObjectUtil.isNull(productList) || productList.size() < productIdList.size()) {
            throw new BizException(ProductError.NOT_EXIST);
        }
        // 计算原始价格
        BigDecimal originalPrice = new BigDecimal(0);
        for (ProductVO product : productList) {
            BigDecimal price = product.getPrice();
            BigDecimal num = new BigDecimal(productNumMap.get(product.getId()));
            BigDecimal productPrice = price.multiply(num);
            originalPrice = originalPrice.add(productPrice);
        }
        order.setOriginalPrice(originalPrice);

        List<Long> couponIdList = query.getCouponIdList();
        // 调用用户服务查询优惠券信息
        List<CouponVO> couponList = userFeignClient.getCouponByIds(couponIdList);
        // 计算最终价格
        BigDecimal payPrice = new BigDecimal(String.valueOf(originalPrice));
        for (CouponVO coupon : couponList) {
            if (coupon.getType() == 1) {
                BigDecimal value = coupon.getValue();
                payPrice = payPrice.subtract(value);
            }
            else {
                BigDecimal discount = coupon.getDiscount();
                payPrice = payPrice.multiply(discount);
            }
        }
        order.setPayPrice(payPrice);
        // 保存订单
        save(order);
        // 保存订单详情
        List<OrderDetail> details = new ArrayList<>(productList.size());
        for (ProductVO product : productList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setNum(productNumMap.get(product.getId()));
            orderDetail.setPrice(product.getPrice().multiply(new BigDecimal(productNumMap.get(product.getId()))));
            details.add(orderDetail);
        }
        orderDetailService.saveBatch(details);
        // 调用商品服务扣减库存
        for (OrderDetailDTO dto : orderDetailDTOList) {
            dto.setNum(-dto.getNum());
            String productMessage = productFeignClient.updateProductStock(dto);
            if (!productMessage.equals("商品库存更新成功")) {
                throw new BizException(ProductError.STOCK_INSUFFICIENT, "出现错误，商品库存可能不足");
            }
        }
        // 更新优惠券状态
        rabbitTemplate.convertAndSend(COUPON_EXCHANGE, COUPON_USE_KEY, couponIdList);
        // 清理购物车
        rabbitTemplate.convertAndSend(CART_EXCHANGE, CART_CLEAR_KEY, productIdList);
        // 检测订单支付状态（延迟消息取消超时订单）
        OrderDelayMessage orderDelayMessage = new OrderDelayMessage();
        orderDelayMessage.setOrderId(order.getId());
        orderDelayMessage.setCouponIds(couponIdList);
        rabbitTemplate.convertAndSend(
                ORDER_EXCHANGE,
                ORDER_DELAY_KEY,
                orderDelayMessage,
                message -> {
                    message.getMessageProperties().setDelayLong(600000L);
                    return message;
                }
        );

        return order.getId();
    }

    @Override
    public void updateStatusById(StatusQuery query) {
        // 更新订单状态
        Order order = new Order();
        order.setId(query.getId());
        order.setStatus(query.getStatus());
        updateById(order);
    }

    @Override
    @GlobalTransactional
    public void cancelOrder(Long orderId) {
        // 将订单状态设置为已关闭
        Order order = baseMapper.selectById(orderId);
        order.setStatus(OrderStatus.CLOSED.getCode());
        updateById(order);
        // 恢复库存
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(orderDetailList)) {
            List<OrderDetailDTO> orderDetailDTOList = orderDetailList.stream()
                    .map(orderDetail -> {
                        OrderDetailDTO dto = new OrderDetailDTO();
                        BeanUtil.copyProperties(orderDetail, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            for (OrderDetailDTO dto : orderDetailDTOList) {
                productFeignClient.updateProductStock(dto);
            }
        }
        // 恢复用户优惠券
        List<Long> couponIdList = order.getCouponIdList();
        userFeignClient.resetCouponStatusByIds(couponIdList);
    }
}
