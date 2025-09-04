package com.wx2.college.model.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CollegeHotRankVO {
    private Integer rankType;
    private Long collegeId;
    private Integer rank;
    private Integer hotScore;
    private LocalDate rankDate;
    private String collegeName;
}
