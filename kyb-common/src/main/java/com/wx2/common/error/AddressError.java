package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum AddressError implements BizError {
    NOT_EXIST(1201, "地址不存在"),
    NOT_EXIST_OR_NO_PERMISSION(1202, "地址不存在或无权操作");

    private final int code;
    private final String message;

    AddressError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
