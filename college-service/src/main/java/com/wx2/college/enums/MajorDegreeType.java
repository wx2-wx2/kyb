package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MajorDegreeType {
    ACADEMIC(1, "学硕"),
    PROFESSIONAL(2, "专硕");

    private final Integer code;
    private final String desc;

    public static MajorDegreeType getByCode(Integer code) {
        for (MajorDegreeType degreeType : values()) {
            if (degreeType.code.equals(code)) {
                return degreeType;
            }
        }
        return null;
    }

}
