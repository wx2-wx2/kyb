package com.wx2.college.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 院校专业基础信息实体类
 */
@Data
@TableName("college_major")
public class CollegeMajor implements Serializable {
    private static final long serialVersionUID = 1L;
    // 专业id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 院校id
    private Long collegeId;
    // 专业代码
    private Integer majorCode;
    // 专业名称
    private String majorName;
    // 学位类型（1-学硕/2-专硕）
    private Integer degreeType;
    // 学科门类（1-工学/2-理学/3-文学/4-经济学/5-管理学/6-医学/7-法学/8-教育学）
    private Integer discipline;
    // 专业简介
    private String introduction;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
