package com.wx2.question.model.query;

import com.wx2.common.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionPageQuery extends PageQuery {
    private String content;
    private Integer difficulty;
    private Integer subject;
    private Integer type;
    private Integer isReal;
    private String knowledgePoint;
}
