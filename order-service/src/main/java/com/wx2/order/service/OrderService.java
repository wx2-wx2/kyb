package com.wx2.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.order.model.entity.Order;
import com.wx2.order.model.query.OrderQuery;

public interface OrderService extends IService<Order> {
    /**
     * 创建订单
     */
    Long addOrder(OrderQuery query);

    /**
     * 更新订单状态
     */
    void updateStatusById(StatusQuery query);

    /**
     * 超时取消订单
     */
    void cancelOrder(Long orderId);
}
