package com.wx2.common.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdQuery {
    @NotNull(message = "请求id不能为空")
    private Long id;
}
