package com.wx2.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer category;
    private String subject;
    private BigDecimal price;
    private Integer stock;
    private Integer sold;
    private Integer status;
}
