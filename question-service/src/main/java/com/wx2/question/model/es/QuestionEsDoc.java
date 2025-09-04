package com.wx2.question.model.es;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionEsDoc implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> knowledgeList;
}
