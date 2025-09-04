package com.wx2.college.model.vo;

import lombok.Data;

@Data
public class MajorDetailVO {
    private Long id;
    private Long majorId;
    private Integer year;
    private String researchDirection;
    private Integer scoreLine;
    private Integer enrollment;
    private Integer actualEnrollment;
    private Integer applyCount;
}
