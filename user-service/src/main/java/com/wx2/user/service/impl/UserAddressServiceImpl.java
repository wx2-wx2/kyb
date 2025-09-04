package com.wx2.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wx2.common.error.AddressError;
import com.wx2.common.exception.BizException;
import com.wx2.common.utils.UserContext;
import com.wx2.user.mapper.UserAddressMapper;
import com.wx2.user.model.entity.UserAddress;
import com.wx2.user.model.query.UserAddressQuery;
import com.wx2.user.model.vo.UserAddressVO;
import com.wx2.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddressVO> getUserAddress() {
        // 查询所有用户地址
        Long userId = UserContext.getUserId();
        List<UserAddress> userAddressList = userAddressMapper.selectByUserId(userId);

        return userAddressList.stream()
                .map(address -> {
                    UserAddressVO vo = new UserAddressVO();
                    BeanUtil.copyProperties(address, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserAddressVO getUserAddressById(Long addressId) {
        // 查询用户地址
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        return BeanUtil.copyProperties(userAddress, UserAddressVO.class);
    }

    @Override
    @Transactional
    public void addUserAddress(UserAddressQuery query) {
        Long userId = UserContext.getUserId();
        UserAddress address = BeanUtil.copyProperties(query, UserAddress.class);
        address.setUserId(userId);
        // 如果新增用户地址为默认地址，将用户其他地址设置为非默认
        if (address.getIsDefault() == 1) {
            userAddressMapper.updateNonDefaultByUserId(userId);
        }
        // 新增用户地址
        userAddressMapper.insert(address);
    }

    @Override
    @Transactional
    public void updateUserAddress(UserAddressQuery query) {
        Long userId = UserContext.getUserId();
        Long addressId = query.getAddressId();
        // 根据地址id和用户id查询用户地址
        UserAddress address = userAddressMapper.selectByIdAndUserId(addressId, userId);
        // 检验用户地址是否存在
        if (ObjectUtil.isNull(address)) {
            throw new BizException(AddressError.NOT_EXIST_OR_NO_PERMISSION);
        }
        BeanUtil.copyProperties(query, address);
        // 如果将此用户地址设置为默认地址，将用户其他地址设置为非默认
        if (address.getIsDefault() == 1) {
            userAddressMapper.updateNonDefaultByUserId(userId);
        }
        // 更新用户地址
        userAddressMapper.updateById(address);
    }

    @Override
    @Transactional
    public void deleteUserAddress(Long addressId) {
        Long userId = UserContext.getUserId();
        // 根据地址id和用户id查询用户地址
        UserAddress address = userAddressMapper.selectByIdAndUserId(addressId, userId);
        // 检验用户地址是否存在
        if (ObjectUtil.isNull(address)) {
            throw new BizException(AddressError.NOT_EXIST_OR_NO_PERMISSION);
        }
        // 根据地址id删除用户地址
        userAddressMapper.deleteById(addressId);
    }

    @Override
    @Transactional
    public void setDefaultUserAddress(Long addressId) {
        Long userId = UserContext.getUserId();
        // 根据地址id和用户id查询用户地址
        UserAddress address = userAddressMapper.selectByIdAndUserId(addressId, userId);
        // 检验用户地址是否存在
        if (ObjectUtil.isNull(address)) {
            throw new BizException(AddressError.NOT_EXIST_OR_NO_PERMISSION);
        }
        // 将用户其他地址设置为非默认
        userAddressMapper.updateNonDefaultByUserId(userId);
        // 将此用户地址设置为默认地址
        address.setIsDefault(1);
        userAddressMapper.updateById(address);
    }
}
