package com.wx2.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentVO {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Integer type;
    private Integer status;
    private String thirdPartyTradeNo;
    private LocalDateTime payTime;
    private String callbackContent;
}
