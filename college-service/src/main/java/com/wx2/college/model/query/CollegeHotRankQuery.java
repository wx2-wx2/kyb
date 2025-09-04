package com.wx2.college.model.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CollegeHotRankQuery {
    @NotNull(message = "榜单类型不能为空")
    @Min(value = 1, message = "榜单类型取值：1-日榜/2-周榜")
    @Max(value = 2, message = "榜单类型取值：1-日榜/2-周榜")
    private Integer rankType;
    @NotNull(message = "榜单日期不能为空")
    private LocalDate rankDate;
}
