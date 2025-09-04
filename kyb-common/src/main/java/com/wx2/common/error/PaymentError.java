package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum PaymentError implements BizError{
    TYPE_INVALID(6001, "类型不合法"),
    PAYING(6002, "正在支付中"),
    ALREADY_PAY(6003, "订单已支付"),
    FAIL_PAY(6004, "订单支付失败");

    private final int code;
    private final String message;

    PaymentError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
