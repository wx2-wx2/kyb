package com.wx2.college.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 院校搜索行为日志实体类
 */
@Data
@TableName("college_search_log")
public class CollegeSearchLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // 日志id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 院校id
    private Long collegeId;
    // 关键词
    private String keyword;
    // 用户ip
    private String ip;
    // 是否点击结果（0-否/1-是）
    private Integer isClick;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
