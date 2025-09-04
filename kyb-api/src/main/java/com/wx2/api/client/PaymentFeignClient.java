package com.wx2.api.client;

import com.wx2.api.client.fallback.PaymentFeignClientFallbackFactory;
import com.wx2.api.config.FeignConfig;
import com.wx2.common.model.vo.PaymentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "payment-service",
        configuration = FeignConfig.class,
        fallbackFactory = PaymentFeignClientFallbackFactory.class
)
public interface PaymentFeignClient {
    @GetMapping("/api/payment/by-order/{orderId}")
    PaymentVO getPaymentByOrderId(@PathVariable Long orderId);
}
