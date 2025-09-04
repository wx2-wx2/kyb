package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeLevel {
    LEVEL_985(1, "985"),
    LEVEL_211(2, "211"),
    LEVEL_DOUBLE_FIRST_CLASS(3, "双一流"),
    LEVEL_DOUBLE_NON(4, "普通");

    private final Integer code;
    private final String desc;

    public static CollegeLevel getByCode(Integer code) {
        for (CollegeLevel level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        return null;
    }

}
