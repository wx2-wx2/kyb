package com.wx2.user.mapper;

import com.wx2.user.model.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddressMapper {
    /**
     * 根据用户id查询地址
     */
    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据地址id查询地址
     */
    UserAddress selectById(@Param("id") Long id);

    /**
     * 修改用户所有地址为非默认
     */
    void updateNonDefaultByUserId(@Param("userId") Long userId);

    /**
     * 新增地址
     */
    void insert(UserAddress address);

    /**
     * 根据地址id和用户id查询用户地址
     */
    UserAddress selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 更新用户地址
     */
    void updateById(UserAddress address);

    /**
     * 根据地址id删除用户地址
     */
    void deleteById(@Param("id") Long id);
}
