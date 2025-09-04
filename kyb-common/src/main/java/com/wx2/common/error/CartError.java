package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum CartError implements BizError {
    REACH_MAX(5001, "购物车条目数达到最大值");

    private final int code;
    private final String message;

    CartError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
