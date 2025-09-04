package com.wx2.user.mapper;

import com.wx2.user.model.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

public interface UserProfileMapper {
    /**
     * 根据用户id查询用户详细信息
     */
    UserProfile selectByUserId(@Param("userId") Long userId);

    /**
     * 新增用户详细信息
     */
    void insertByUserId(UserProfile userProfile);

    /**
     * 更新用户详细信息
     */
    void updateByUserId(UserProfile userProfile);
}
