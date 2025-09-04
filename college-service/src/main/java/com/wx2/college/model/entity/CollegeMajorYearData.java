package com.wx2.college.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 院校专业年份数据实体类
 */
@Data
@TableName("college_major_year_data")
public class CollegeMajorYearData implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 专业id
    private Long majorId;
    // 年份
    private Integer year;
    // 研究方向
    private String researchDirection;
    // 复试分数线
    private Integer scoreLine;
    // 招生人数
    private Integer enrollment;
    // 录取人数
    private Integer actualEnrollment;
    // 报考人数
    private Integer applyCount;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
