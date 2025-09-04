package com.wx2.question.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubjectEnum {
    MATH(1, "数学"),
    ENGLISH(2, "英语"),
    POLITICS(3, "政治");

    private final Integer code;
    private final String desc;
}
