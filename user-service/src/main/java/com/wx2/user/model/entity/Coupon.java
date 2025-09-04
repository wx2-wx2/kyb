package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 优惠券表实体类
 */
@Data
@TableName("coupon")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;
    // 优惠券id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 优惠券名称
    private String name;
    // 类型（1-满减/2-折扣）
    private Integer type;
    // 面额
    private BigDecimal value;
    // 折扣
    private BigDecimal discount;
    // 门槛
    private BigDecimal threshold;
    // 开始时间
    private LocalDate startTime;
    // 结束时间
    private LocalDate endTime;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
