package com.wx2.question.model.query;

import lombok.Data;

@Data
public class QuestionQuery {
    private Long id;
    private String content;
    private String image;
    private Integer difficulty;
    private Integer subject;
    private Integer type;
    private Integer isReal;
    private String realInformation;
    private String answer;
    private String video;
}
