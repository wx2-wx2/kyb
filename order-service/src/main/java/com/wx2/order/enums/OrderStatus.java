package com.wx2.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    UNPAID(1, "未支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    ARRIVED(4, "已到达"),
    RECEIVED(5, "已收货"),
    EVALUATED(6, "已评价"),
    CLOSED(7, "已关闭");

    private final Integer code;
    private final String desc;
}
