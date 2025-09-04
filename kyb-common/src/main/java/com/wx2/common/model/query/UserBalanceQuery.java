package com.wx2.common.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceQuery {
    @NotNull(message = "用户id不能为空")
    private Long userId;
    @NotNull(message = "余额变动不能为空")
    private BigDecimal amount;
    private String password;
}
