package com.wx2.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponVO {
    private Long id;
    private String name;
    private Integer type;
    private BigDecimal value;
    private BigDecimal discount;
    private BigDecimal threshold;
    private LocalDate startTime;
    private LocalDate endTime;
}
