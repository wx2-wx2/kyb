package com.wx2.user.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginQuery {
    @NotNull(message = "账号不能为空")
    private String account;
    @NotNull(message = "密码不能为空")
    private String password;
    private Boolean rememberMe = false;
}
