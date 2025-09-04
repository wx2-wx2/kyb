package com.wx2.common.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartNumQuery {
    @NotNull(message = "商品id不能为空")
    private Long productId;
    @NotNull(message = "商品数量不能为空")
    private Integer num;
}
