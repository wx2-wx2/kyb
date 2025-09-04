package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户详细信息表实体类
 */
@Data
@TableName("user_profile")
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 昵称
    private String nickname;
    // 头像url
    private String avatar;
    // 性别（0-未知/1-男/2-女）
    private Integer gender;
    // 生日
    private LocalDate birthday;
    // 目标院校
    private String targetSchool;
    // 目标专业
    private String targetMajor;
    // 报考年份
    private Integer examYear;
    // 备考阶段（0-基础/1-强化/2-冲刺）
    private Integer studyStage;
    // 签名
    private String signature;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
