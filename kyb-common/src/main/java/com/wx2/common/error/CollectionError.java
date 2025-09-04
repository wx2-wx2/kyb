package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum CollectionError implements BizError{
    NOT_EXIST(1401, "用户未收藏"),
    ALREADY_EXIST(1402, "用户已收藏");

    private final int code;
    private final String message;

    CollectionError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
