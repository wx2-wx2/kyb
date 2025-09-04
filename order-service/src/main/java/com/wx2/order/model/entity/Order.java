package com.wx2.order.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wx2.order.handler.ListLongTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单表实体类
 */
@Data
@TableName("`order`")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    // 订单id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 原价
    private BigDecimal originalPrice;
    // 优惠券
    @TableField(value = "coupon_id_list", typeHandler = ListLongTypeHandler.class)
    private List<Long> couponIdList;
    // 总价
    private BigDecimal payPrice;
    // 支付类型（1-余额/2-微信/3-支付宝）
    private Integer type;
    // 订单状态（1-未支付/2-已支付/3-已发货/4-已到达/5-已收货/6-已评价/7-已关闭）
    private Integer status;
    // 支付时间
    private LocalDateTime payTime;
    // 发货时间
    private LocalDateTime shippingTime;
    // 到达时间
    private LocalDateTime arrivalTime;
    // 收货时间
    private LocalDateTime receiveTime;
    // 评价时间
    private LocalDateTime commentTime;
    // 关闭时间
    private LocalDateTime closeTime;
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
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
