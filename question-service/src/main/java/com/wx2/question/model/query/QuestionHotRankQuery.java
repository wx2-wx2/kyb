package com.wx2.question.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionHotRankQuery {
    @NotNull(message = "top数不能为空")
    private Integer topN;
    @NotNull(message = "学科不能为空")
    private Integer subject;
}
