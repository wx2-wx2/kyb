package com.wx2.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.order.model.entity.OrderDetail;
import com.wx2.order.model.vo.OrderDetailVO;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 查询订单详情
     */
    List<OrderDetailVO> getOrderDetailByOrderId(Long orderId);
}
