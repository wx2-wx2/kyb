package com.wx2.api.client.fallback;

import com.wx2.api.client.UserFeignClient;
import com.wx2.common.error.FeignClientError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.common.model.vo.CouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public List<CouponVO> getCouponByIds(List<Long> couponIds) {
                log.error("获取优惠券失败", cause);
                return List.of();
            }

            @Override
            public String updateCouponStatusByIds(List<Long> couponIds) {
                log.error("更新用户优惠券状态失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }

            @Override
            public String resetCouponStatusByIds(List<Long> couponIds) {
                log.error("重置用户优惠券状态失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }

            @Override
            public String deductBalance(UserBalanceQuery query) {
                log.error("扣减余额失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }
        };
    }
}
