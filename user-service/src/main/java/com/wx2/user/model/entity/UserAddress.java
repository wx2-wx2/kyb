package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户地址表实体类
 */
@Data
@TableName("user_address")
public class UserAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    // 地址id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 收货人
    private String receiver;
    // 手机号
    private String phone;
    // 省
    private String province;
    // 市
    private String city;
    // 区/县
    private String district;
    // 详细地址
    private String detailAddress;
    // 是否默认
    private Integer isDefault = 0;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
