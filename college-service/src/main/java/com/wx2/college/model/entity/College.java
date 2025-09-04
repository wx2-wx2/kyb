package com.wx2.college.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 院校基础信息实体类
 */
@Data
@TableName("college")
public class College implements Serializable {
    private static final long serialVersionUID = 1L;
    // 院校id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 院校代码
    private String collegeCode;
    // 院校名称
    private String collegeName;
    // 院校简称
    private String shortName;
    // 所属省份
    private String province;
    // 所属城市
    private String city;
    // 院校层次（1-985/2-211/3-双一流/4-普通高校）
    private Integer level;
    // 院校类型（1-综合/2-理工/3-师范）
    private Integer type;
    // 办学性质（1-公办/2-民办）
    private Integer nature;
    // 是否有研究生院（0-否/1-是）
    private Integer hasGraduateSchool;
    // 院校简介
    private String introduction;
    // 热度值
    private Integer hotScore;
    // 收藏数
    private Integer collectCount;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
