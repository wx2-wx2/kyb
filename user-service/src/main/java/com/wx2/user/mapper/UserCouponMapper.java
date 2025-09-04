package com.wx2.user.mapper;

import com.wx2.user.model.entity.UserCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCouponMapper {
    /**
     * 批量更新用户优惠券状态
     */
    void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 新增用户优惠券
     */
    void insert(UserCoupon userCoupon);

    /**
     * 批量查询用户优惠券
     */
    List<UserCoupon> selectByIds(List<Long> ids);
}
