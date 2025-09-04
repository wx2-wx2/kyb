package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum CouponError implements BizError {
    NOT_EXIST(1301, "不存在此模板优惠券"),
    STATUS_ERROR(1302, "用户优惠券状态有误");

    private final int code;
    private final String message;

    CouponError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
