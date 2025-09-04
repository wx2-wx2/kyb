package com.wx2.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户院校收藏表实体类
 */
@Data
@TableName("user_collection_college")
public class UserCollectionCollege implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    // 用户id
    private Long userId;
    // 院校id
    private Long collegeId;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
