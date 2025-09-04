package com.wx2.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    NORMAL(1, "普通用户"),
    VIP(2, "vip用户"),
    ADMIN(3, "管理员");

    private final Integer code;
    private final String desc;

}
