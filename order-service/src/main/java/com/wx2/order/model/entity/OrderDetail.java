package com.wx2.order.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情表实体类
 */
@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    // 订单详情id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 订单id
    private Long orderId;
    // 商品id
    private Long productId;
    // 商品名称
    private String productName;
    // 购买数量
    private Integer num;
    // 价格
    private BigDecimal price;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
