package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MajorDiscipline {
    ENGINEERING(1, "工学"),
    SCIENCE(2, "理学"),
    LITERATURE(3, "文学"),
    ECONOMICS(4, "经济学"),
    MANAGEMENT(5, "管理学"),
    MEDICINE(6, "医学"),
    LAW(7, "法学"),
    EDUCATION(8, "教育学");

    private final Integer code;
    private final String desc;

    public static MajorDiscipline getByCode(Integer code) {
        for (MajorDiscipline discipline : values()) {
            if (discipline.code.equals(code)) {
                return discipline;
            }
        }
        return null;
    }

}
