package com.wx2.common.model.vo;

import lombok.Data;

@Data
public class MajorVO {
    private Long id;
    private String majorCode;
    private String majorName;
    private Integer degreeType;
    private Integer discipline;
    private String introduction;
    private String degreeTypeDesc;
    private String disciplineDesc;
}
