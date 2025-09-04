package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum ProductError implements BizError{
    NOT_EXIST(4001, "商品不存在"),
    STATUS_ERROR(4002, "商品状态异常"),
    STOCK_INSUFFICIENT(4003, "商品库存不足");

    private final int code;
    private final String message;

    ProductError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
