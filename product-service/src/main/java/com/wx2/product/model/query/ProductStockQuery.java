package com.wx2.product.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductStockQuery {
    @NotNull(message = "商品id不能为空")
    private Long productId;
    @NotNull(message = "库存变更不能为空")
    private Integer stock;
}
