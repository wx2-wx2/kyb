package com.wx2.cart.model.query;

import lombok.Data;

@Data
public class CartQuery {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer num;
}
