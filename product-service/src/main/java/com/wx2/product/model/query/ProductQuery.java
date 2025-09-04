package com.wx2.product.model.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductQuery {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer category;
    private String subject;
    private BigDecimal price;
    private Integer sold;
}
