package com.wx2.question.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目知识点关联表实体类
 */
@Data
@TableName("relation")
public class Relation implements Serializable {
    private static final long serialVersionUID = 1L;
    // 联合主键：题目id
    private Long questionId;
    // 联合主键：知识点id
    private Long knowledgeId;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
