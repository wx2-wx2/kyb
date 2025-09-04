package com.wx2.api.client.fallback;

import com.wx2.api.client.CartFeignClient;
import com.wx2.common.error.FeignClientError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.query.CartNumQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CartFeignClientFallbackFactory implements FallbackFactory<CartFeignClient> {
    @Override
    public CartFeignClient create(Throwable cause) {
        return new CartFeignClient() {
            @Override
            public String deleteFromCartByProductIds(List<Long> productIds) {
                log.error("从购物车中删除商品失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }

            @Override
            public String addToCart(CartNumQuery query) {
                log.error("添加商品到购物车失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }
        };
    }
}
