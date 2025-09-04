package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeNature {
    PUBLIC(1, "公办"),
    PRIVATE(2, "民办");

    private final Integer code;
    private final String desc;

    public static CollegeNature getByCode(Integer code) {
        for (CollegeNature nature : values()) {
            if (nature.code.equals(code)) {
                return nature;
            }
        }
        return null;
    }

}
