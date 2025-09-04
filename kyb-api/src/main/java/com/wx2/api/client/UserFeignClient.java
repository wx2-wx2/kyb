package com.wx2.api.client;

import com.wx2.api.client.fallback.UserFeignClientFallbackFactory;
import com.wx2.api.config.FeignConfig;
import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.common.model.vo.CouponVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "user-service",
        configuration = FeignConfig.class,
        fallbackFactory = UserFeignClientFallbackFactory.class
)
public interface UserFeignClient {
    @PostMapping("/api/coupon/batch-get")
    List<CouponVO> getCouponByIds(@RequestParam("couponIds") List<Long> couponIds);

    @PutMapping("/api/coupon/status/use")
    String updateCouponStatusByIds(@RequestParam("couponIds") List<Long> couponIds);

    @PutMapping("/api/coupon/status/reset")
    String resetCouponStatusByIds(@RequestParam("couponIds") List<Long> couponIds);

    @PutMapping("/api/user/balance/deduct")
    String deductBalance(@Valid @RequestBody UserBalanceQuery query);
}
