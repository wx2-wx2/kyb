package com.wx2.payment.model.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentQuery {
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Integer type;
}
