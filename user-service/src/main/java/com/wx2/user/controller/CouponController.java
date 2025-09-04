package com.wx2.user.controller;

import com.wx2.common.model.query.IdQuery;
import com.wx2.common.model.vo.CouponVO;
import com.wx2.user.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    /**
     * 根据优惠券id批量查询优惠券
     */
    @PostMapping("/batch-get")
    public List<CouponVO> getCouponByIds(@RequestParam("couponIds") List<Long> couponIds) {
        return couponService.getCouponByIds(couponIds);
    }

    /**
     * 更新优惠券状态为已使用
     */
    @PutMapping("/status/use")
    public String updateCouponStatusByIds(@RequestParam("couponIds") List<Long> couponIds) {
        couponService.updateCouponStatusByIds(couponIds, 1);
        return "更新优惠券状态成功";
    }

    /**
     * 重置优惠券状态为未使用
     */
    @PutMapping("/status/reset")
    public String resetCouponStatusByIds(@RequestParam("couponIds") List<Long> couponIds) {
        couponService.updateCouponStatusByIds(couponIds, 0);
        return "更新优惠券状态成功";
    }

    /**
     * 领取优惠券
     */
    @PostMapping("/add")
    public String addUserCoupon(@Valid @RequestBody IdQuery request) {
        couponService.addUserCoupon(request.getId());
        return "领取优惠券成功";
    }
}
