package com.wx2.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserGender {
    UNKNOWN(0, "未知"),
    BOY(1, "男"),
    GIRL(2, "女");

    private final Integer code;
    private final String desc;

}
