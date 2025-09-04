package com.wx2.question.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识点表实体类
 */
@Data
@TableName("knowledge")
public class Knowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    // 知识点id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 知识点名称
    private String name;
    // 所属学科（1-数学/2-英语/3-政治）
    private Integer subject;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
