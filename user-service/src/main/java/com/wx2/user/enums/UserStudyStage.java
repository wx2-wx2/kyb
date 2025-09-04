package com.wx2.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStudyStage {
    BASIC(0, "基础"),
    STRENGTHEN(1, "强化"),
    SPRINT(2, "冲刺");

    private final Integer code;
    private final String desc;

}
