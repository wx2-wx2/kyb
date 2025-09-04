package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeType {
    COMPREHENSIVE(1, "综合类"),
    SCIENCE_AND_ENGINEERING(2, "理工类"),
    NORMAL(3, "师范类"),
    MEDICAL(4, "医学类"),
    FINANCIAL(5, "财经类"),
    AGRICULTURAL(6, "农林类");

    private final Integer code;
    private final String desc;

    public static CollegeType getByCode(Integer code) {
        for (CollegeType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}
