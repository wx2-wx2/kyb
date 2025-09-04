package com.wx2.question.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class QuestionVO {
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
    private List<String> knowledgeList;
}
