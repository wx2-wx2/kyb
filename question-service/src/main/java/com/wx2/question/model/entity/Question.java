package com.wx2.question.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目表实体类
 */
@Data
@TableName("question")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    // 题目id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 题目内容
    private String content;
    // 题目图片
    private String image;
    // 难度（1-简答/2-中等/3-困难）
    private Integer difficulty;
    // 学科（1-数学/2-英语/3-政治）
    private Integer subject;
    // 类型（1-选择/2-填空/3-判断/4-解答）
    private Integer type;
    // 是否是真题（0-否/1-是）
    private Integer isReal;
    // 真题信息
    private String realInformation;
    // 答案
    private String answer;
    // 视频解析
    private String video;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
