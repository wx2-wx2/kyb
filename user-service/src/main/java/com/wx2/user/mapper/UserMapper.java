package com.wx2.user.mapper;

import com.wx2.user.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserMapper {
    /**
     * 插入新用户
     */
    void insert(User user);

    /**
     * 根据用户id查询用户核心信息
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用username或phone查询用户核心信息
     */
    User selectByAccount(@Param("account") String account);

    /**
     * 更新用户最近登录时间
     */
    void updateLastLoginTime(@Param("id") Long id);

    /**
     * 更新用户余额
     */
    void updateBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
