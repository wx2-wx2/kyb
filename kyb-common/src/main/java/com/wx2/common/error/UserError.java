package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum UserError implements BizError{
    NOT_EXIST(1001, "用户不存在"),
    STATUS_ERROR(1002, "用户状态异常"),
    USERNAME_DUPLICATE(1003, "用户名已被注册"),
    PHONE_DUPLICATE(1004, "手机号已被注册"),
    PASSWORD_ERROR(1005, "密码错误"),
    BALANCE_INSUFFICIENT(1006, "余额不足"),
    PASSWORD_ERROR_OR_BALANCE_INSUFFICIENT(1007, "密码错误或余额不足"),
    USER_ID_BLANK(1008, "用户id为空");

    private final int code;
    private final String message;

    UserError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
