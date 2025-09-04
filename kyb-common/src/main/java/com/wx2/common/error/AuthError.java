package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum AuthError implements BizError {
    NOT_LOGGED_IN(1101, "未登录"),
    TOKEN_INVALID(1102, "无效的token"),
    TOKEN_EXPIRED(1103, "token已经过期");


    private final int code;
    private final String message;

    AuthError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
