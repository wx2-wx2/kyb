package com.wx2.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    BALANCE(1, "余额"),
    WECHAT(2, "微信"),
    ALIPAY(3, "支付宝");

    private final Integer code;
    private final String desc;

}
