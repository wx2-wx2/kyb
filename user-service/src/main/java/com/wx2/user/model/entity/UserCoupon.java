package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券表实体类
 */
@Data
@TableName("user_coupon")
public class UserCoupon implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 优惠券id
    private Long couponId;
    // 状态（0-未使用/1-已使用/2-已过期）
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
