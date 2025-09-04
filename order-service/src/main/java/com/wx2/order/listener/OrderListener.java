package com.wx2.order.listener;

import cn.hutool.core.util.ObjectUtil;
import com.wx2.api.client.PaymentFeignClient;
import com.wx2.common.model.dto.OrderDelayMessage;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.common.model.vo.PaymentVO;
import com.wx2.order.enums.OrderStatus;
import com.wx2.order.model.entity.Order;
import com.wx2.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.wx2.common.constant.MqConstant.*;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final OrderService orderService;
    private final PaymentFeignClient paymentFeignClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ORDER_QUEUE),
            exchange = @Exchange(name = ORDER_EXCHANGE, delayed = "true"),
            key = ORDER_DELAY_KEY
    ))
    public void listenOrderStatus(OrderDelayMessage orderDelayMessage) {
        Long orderId = orderDelayMessage.getOrderId();
        List<Long> couponIds = orderDelayMessage.getCouponIds();
        // 查询订单信息
        Order order = orderService.getById(orderId);
        // 订单不存在或状态不是未支付，则返回
        if (ObjectUtil.isNull(order) || !Objects.equals(order.getStatus(), OrderStatus.UNPAID.getCode())) {
            return;
        }
        // 调用支付服务查询订单的支付状态
        PaymentVO vo = paymentFeignClient.getPaymentByOrderId(orderId);
        // 支付单存在且支付状态为已支付，则更新订单状态
        if (ObjectUtil.isNotNull(vo) && vo.getStatus() == 3) {
            StatusQuery statusQuery = new StatusQuery();
            statusQuery.setId(orderId);
            statusQuery.setStatus(OrderStatus.PAID.getCode());
            orderService.updateStatusById(statusQuery);
        }
        // 取消订单
        else {
            orderService.cancelOrder(orderId);
        }
    }

}
