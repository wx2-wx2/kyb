package com.wx2.order.model.query;

import com.wx2.common.model.dto.OrderDetailDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderQuery {
    private BigDecimal originalPrice;
    private List<Long> couponIdList;
    private BigDecimal payPrice;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private List<OrderDetailDTO> detailList;
}
