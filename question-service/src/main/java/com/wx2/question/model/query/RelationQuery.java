package com.wx2.question.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelationQuery {
    @NotNull(message = "题目id不能为空")
    private Long questionId;
    @NotNull(message = "知识点id不能为空")
    private Long knowledgeId;
}
