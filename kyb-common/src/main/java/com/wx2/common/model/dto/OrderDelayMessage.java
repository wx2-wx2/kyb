package com.wx2.common.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDelayMessage {
    private Long orderId;
    private List<Long> couponIds;
}
