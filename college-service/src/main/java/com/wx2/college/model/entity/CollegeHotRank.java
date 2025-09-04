package com.wx2.college.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 院校热度榜单实体类
 */
@Data
@TableName("college_hot_rank")
public class CollegeHotRank implements Serializable {
    private static final long serialVersionUID = 1L;
    // 榜单id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 榜单类型（1-日榜/2-周榜/3-月榜）
    private Integer rankType;
    // 院校id
    private Long collegeId;
    // 排名
    private Integer rank;
    // 热度值
    private Integer hotScore;
    // 榜单日期
    private LocalDate rankDate;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
