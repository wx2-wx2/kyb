package com.wx2.user.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignQuery {
    @NotNull(message = "年份不能为空")
    private Integer year;
    @NotNull(message = "月份不能为空")
    private Integer month;
}
