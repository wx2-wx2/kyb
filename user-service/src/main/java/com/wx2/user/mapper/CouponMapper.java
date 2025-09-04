package com.wx2.user.mapper;

import com.wx2.user.model.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponMapper {

    /**
     * 批量查询优惠券
     */
    List<Coupon> selectByIds(List<Long> ids);

    /**
     * 根据优惠券id查询优惠券
     */
    Coupon selectById(@Param("id") Long id);
}
