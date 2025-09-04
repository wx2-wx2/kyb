package com.wx2.api.client.fallback;

import com.wx2.api.client.PaymentFeignClient;
import com.wx2.common.model.vo.PaymentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFeignClientFallbackFactory implements FallbackFactory<PaymentFeignClient> {
    @Override
    public PaymentFeignClient create(Throwable cause) {
        return new PaymentFeignClient() {
            @Override
            public PaymentVO getPaymentByOrderId(Long orderId) {
                log.error("获取支付单失败", cause);
                return new PaymentVO();
            }
        };
    }
}
