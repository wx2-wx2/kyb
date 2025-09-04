package com.wx2.question.model.vo;

import lombok.Data;

@Data
public class QuestionHotRankVO {
    private Integer rank;
    private Long questionId;
    private String content;
    private Long hotScore;
}
