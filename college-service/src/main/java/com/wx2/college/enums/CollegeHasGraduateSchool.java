package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeHasGraduateSchool {
    NO(0, "否"),
    YES(1, "是");

    private final Integer code;
    private final String desc;

    public static CollegeHasGraduateSchool getByCode(Integer code) {
        for (CollegeHasGraduateSchool hasGraduateSchool : values()) {
            if (hasGraduateSchool.code.equals(code)) {
                return hasGraduateSchool;
            }
        }
        return null;
    }

}
