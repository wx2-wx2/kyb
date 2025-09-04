package com.wx2.common.model.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PageQuery {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT_DIRECTION = "asc";

    @Min(value = 1, message = "页码不能小于1")
    private Integer page = DEFAULT_PAGE;

    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer size = DEFAULT_SIZE;

    @Pattern(
        regexp = "^[a-zA-Z0-9_]*$",
        message = "排序字段格式不合法，仅允许字母、数字和下划线"
    )
    private String sortField;

    @Pattern(
        regexp = "^(asc|desc)$",
        message = "排序方向不合法，仅允许asc或desc"
    )
    private String sortDirection = DEFAULT_SORT_DIRECTION;

    public int getOffset() {
        return (page - 1) * size;
    }
}
