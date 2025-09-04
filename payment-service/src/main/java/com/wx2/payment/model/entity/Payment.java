package com.wx2.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付表实体类
 */
@Data
@TableName("payment")
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    // 支付单id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 订单id
    private Long orderId;
    // 用户id
    private Long userId;
    // 支付金额
    private BigDecimal amount;
    // 支付方式（1-余额/2-微信/3-支付宝）
    private Integer type;
    // 状态（0-未支付/1-支付中/2-支付成功/3-支付失败）
    private Integer status;
    // 第三方支付流水号
    private String thirdPartyTradeNo;
    // 支付时间
    private LocalDateTime payTime;
    // 支付回调内容
    private String callbackContent;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
