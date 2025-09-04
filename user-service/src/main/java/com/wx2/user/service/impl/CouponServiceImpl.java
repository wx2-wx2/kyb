package com.wx2.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.wx2.common.error.CouponError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.vo.CouponVO;
import com.wx2.common.utils.UserContext;
import com.wx2.user.mapper.CouponMapper;
import com.wx2.user.mapper.UserCouponMapper;
import com.wx2.user.model.entity.Coupon;
import com.wx2.user.model.entity.UserCoupon;
import com.wx2.user.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    private Snowflake snowflake;

    @PostConstruct
    public void initSnowflake() {
        long workerId = 1;
        long dataCenterId = 1;
        this.snowflake = new Snowflake(workerId, dataCenterId);
    }

    @Override
    public List<CouponVO> getCouponByIds(List<Long> couponIds) {
        // 批量查询优惠券
        List<Coupon> couponList = couponMapper.selectByIds(couponIds);
        return BeanUtil.copyToList(couponList, CouponVO.class);
    }

    @Override
    @Transactional
    public void updateCouponStatusByIds(List<Long> couponIds, Integer status) {
        // 批量查询用户优惠券
        List<UserCoupon> userCouponList = userCouponMapper.selectByIds(couponIds);
        // 检验用户优惠券状态
        for (UserCoupon userCoupon : userCouponList) {
            if (userCoupon.getStatus().equals(status)) {
                throw new BizException(CouponError.STATUS_ERROR);
            }
        }
        // 批量更新用户优惠券状态
        userCouponMapper.updateStatusByIds(couponIds, status);
    }

    @Override
    @Transactional
    public void addUserCoupon(Long couponId) {
        // 查询优惠券
        Coupon coupon = couponMapper.selectById(couponId);
        // 检验优惠券是否存在
        if (ObjectUtil.isNull(coupon)) {
            throw new BizException(CouponError.NOT_EXIST);
        }
        // 使用雪花算法生成用户优惠券id
        Long id = snowflake.nextId();

        Long userId = UserContext.getUserId();
        // 新增用户优惠券
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(id);
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);

        userCouponMapper.insert(userCoupon);
    }
}
