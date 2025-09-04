package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户核心信息表实体类
 */
@Data
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    // 用户id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 角色（1-普通用户/2-VIP用户/3-管理员）
    private Integer role;
    // 状态（1-正常/2-禁用/3-注销）
    private Integer status;
    // 余额
    private BigDecimal balance;
    // 最近登录时间
    private LocalDateTime lastLoginTime;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
