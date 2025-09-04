package com.wx2.product.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表实体类
 */
@Data
@TableName("product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    // 商品id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 商品名称
    private String name;
    // 描述
    private String description;
    // 图片url
    private String image;
    // 类目（1-书籍/2-真题/3-在线课程/4-咨询服务）
    private Integer category;
    // 学科
    private String subject;
    // 价格
    private BigDecimal price;
    // 库存
    private Integer stock;
    // 销量
    private Integer sold;
    // 状态（1-正常/2-下架/3-删除）
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
