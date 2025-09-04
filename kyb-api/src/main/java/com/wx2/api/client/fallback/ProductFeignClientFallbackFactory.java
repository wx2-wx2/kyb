package com.wx2.api.client.fallback;

import com.wx2.api.client.ProductFeignClient;
import com.wx2.common.error.FeignClientError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.PageData;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductFeignClientFallbackFactory implements FallbackFactory<ProductFeignClient> {
    @Override
    public ProductFeignClient create(Throwable cause) {
        return new ProductFeignClient() {
            @Override
            public List<ProductVO> getProductByIds(List<Long> productIds) {
                log.error("获取商品失败", cause);
                return List.of();
            }

            @Override
            public String updateProductStock(OrderDetailDTO dto) {
                log.error("更新商品库存失败", cause);
                throw new BizException(FeignClientError.FEIGN_CLIENT_ERROR);
            }

            @Override
            public PageData<ProductVO> getProductByPage(ProductPageQuery query) {
                log.error("获取商品失败", cause);
                return new PageData<>(List.of(), 0, query);
            }
        };
    }
}
