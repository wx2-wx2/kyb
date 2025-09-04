package com.wx2.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.order.mapper.OrderDetailMapper;
import com.wx2.order.model.entity.OrderDetail;
import com.wx2.order.model.vo.OrderDetailVO;
import com.wx2.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public List<OrderDetailVO> getOrderDetailByOrderId(Long orderId) {
        // 查询订单详情
        List<OrderDetail> orderDetailList = lambdaQuery().eq(OrderDetail::getOrderId, orderId).list();
        return BeanUtil.copyToList(orderDetailList, OrderDetailVO.class);
    }
}
