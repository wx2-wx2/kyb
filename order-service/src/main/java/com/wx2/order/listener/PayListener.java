package com.wx2.order.listener;

import cn.hutool.core.util.ObjectUtil;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.order.enums.OrderStatus;
import com.wx2.order.model.entity.Order;
import com.wx2.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.wx2.common.constant.MqConstant.*;

@Component
@RequiredArgsConstructor
public class PayListener {

    private final OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = PAY_QUEUE),
            exchange = @Exchange(name = PAY_EXCHANGE, type = "topic"),
            key = PAY_SUCCESS_KEY
    ))
    public void paySuccess(Long orderId) {
        // 查询订单信息
        Order order = orderService.getById(orderId);
        // 订单不存在或订单状态不是未支付，则返回
        if (ObjectUtil.isNull(order) || !Objects.equals(order.getStatus(), OrderStatus.UNPAID.getCode())) {
            return;
        }
        // 更新订单状态
        StatusQuery request = new StatusQuery();
        request.setId(orderId);
        request.setStatus(OrderStatus.PAID.getCode());
        orderService.updateStatusById(request);
    }
}
