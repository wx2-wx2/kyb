package com.wx2.common.model.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductPageQuery extends PageQuery {
    private String keyword;
    @Min(value = 1, message = "类目取值范围为1-4")
    @Max(value = 4, message = "类目取值范围为1-4")
    private Integer category;
    private String subject;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
