package com.wx2.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private Long userId;
    private BigDecimal originalPrice;
    private List<Long> couponIdList;
    private BigDecimal payPrice;
    private Integer type;
    private Integer status;
    private LocalDateTime payTime;
    private LocalDateTime shippingTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime receiveTime;
    private LocalDateTime commentTime;
    private LocalDateTime closeTime;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
}
