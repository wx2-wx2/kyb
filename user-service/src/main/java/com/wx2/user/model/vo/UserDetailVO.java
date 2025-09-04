package com.wx2.user.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserDetailVO {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender;
    private LocalDate birthday;
    private String targetSchool;
    private String targetMajor;
    private Integer examYear;
    private String studyStage;
    private String signature;
    private String username;
    private String phone;
    private String email;
    private Integer role;
    private Integer status;
    private BigDecimal balance;
}
