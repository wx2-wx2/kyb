package com.wx2.payment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {
    @NotNull(message = "支付单id不能为空")
    private Long id;
    @NotNull(message = "支付密码不能为空")
    private String password;
}
