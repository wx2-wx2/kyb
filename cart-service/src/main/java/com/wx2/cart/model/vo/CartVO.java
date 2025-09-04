package com.wx2.cart.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer num;
    private String name;
    private String image;
    private BigDecimal price;
}
