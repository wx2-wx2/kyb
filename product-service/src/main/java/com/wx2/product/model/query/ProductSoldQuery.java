package com.wx2.product.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductSoldQuery {
    @NotNull(message = "商品id不能为空")
    private Long productId;
    @NotNull(message = "销量变更不能为空")
    private Integer sold;
}
