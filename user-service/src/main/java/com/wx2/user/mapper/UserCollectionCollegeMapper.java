package com.wx2.user.mapper;

import com.wx2.user.model.entity.UserCollectionCollege;
import org.apache.ibatis.annotations.Param;

public interface UserCollectionCollegeMapper {
    /**
     * 新增收藏院校
     */
    void insert(UserCollectionCollege collection);

    /**
     * 取消收藏院校
     */
    void deleteByUserIdAndCollegeId(@Param("userId") Long userId, @Param("collegeId") Long collegeId);

    /**
     * 根据用户id和院校id查询用户院校收藏关联
     */
    UserCollectionCollege selectUserIdAndCollegeId(@Param("userId") Long userId, @Param("collegeId") Long collegeId);
}
