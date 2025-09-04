package com.wx2.user.service;

import com.wx2.common.model.vo.CouponVO;

import java.util.List;

public interface CouponService {
    /**
     * 根据优惠券id批量查询优惠券
     */
    List<CouponVO> getCouponByIds(List<Long> couponIds);

    /**
     * 更新优惠券状态
     */
    void updateCouponStatusByIds(List<Long> couponIds, Integer status);

    /**
     * 领取优惠券
     */
    void addUserCoupon(Long couponId);
}
