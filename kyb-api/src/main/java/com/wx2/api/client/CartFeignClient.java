package com.wx2.api.client;

import com.wx2.api.client.fallback.CartFeignClientFallbackFactory;
import com.wx2.api.config.FeignConfig;
import com.wx2.common.model.query.CartNumQuery;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cart-service",
        configuration = FeignConfig.class,
        fallbackFactory = CartFeignClientFallbackFactory.class
)
public interface CartFeignClient {
    @DeleteMapping("/api/cart/delete/product")
    String deleteFromCartByProductIds(@RequestParam("productIds") List<Long> productIds);

    @PostMapping("/api/cart/add")
    String addToCart(@Valid @RequestBody CartNumQuery query);
}
