package com.wx2.user.service;

import com.wx2.user.model.query.UserAddressQuery;
import com.wx2.user.model.vo.UserAddressVO;

import java.util.List;

public interface UserAddressService {
    /**
     * 查询用户所有地址
     */
    List<UserAddressVO> getUserAddress();

    /**
     * 根据地址id查询地址
     */
    UserAddressVO getUserAddressById(Long addressId);

    /**
     * 新增用户地址
     */
    void addUserAddress(UserAddressQuery query);

    /**
     * 修改用户地址
     */
    void updateUserAddress(UserAddressQuery query);

    /**
     * 删除用户地址
     */
    void deleteUserAddress(Long addressId);

    /**
     * 设置用户默认地址
     */
    void setDefaultUserAddress(Long addressId);
}
