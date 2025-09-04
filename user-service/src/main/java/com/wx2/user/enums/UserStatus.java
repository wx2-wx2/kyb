package com.wx2.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    NORMAL(1, "正常"),
    DISABLE(2, "禁用"),
    LOGOUT(3, "注销");

    private final Integer code;
    private final String desc;

}
