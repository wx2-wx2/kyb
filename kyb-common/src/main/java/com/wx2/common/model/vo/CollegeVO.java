package com.wx2.common.model.vo;

import lombok.Data;

@Data
public class CollegeVO {
    private Long id;
    private String collegeCode;
    private String collegeName;
    private String shortName;
    private String province;
    private String city;
    private Integer level;
    private Integer type;
    private Integer nature;
    private Integer hasGraduateSchool;
    private String introduction;
    private Integer hotScore;
    private String levelDesc;
    private String typeDesc;
    private String natureDesc;
    private String hasGraduateSchoolDesc;
}
