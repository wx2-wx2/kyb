package com.wx2.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailVO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer num;
    private BigDecimal price;
}
