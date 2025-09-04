package com.wx2.api.client;

import com.wx2.api.client.fallback.ProductFeignClientFallbackFactory;
import com.wx2.api.config.FeignConfig;
import com.wx2.common.model.PageData;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.vo.ProductVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "product-service",
        configuration = FeignConfig.class,
        fallbackFactory = ProductFeignClientFallbackFactory.class
)
public interface ProductFeignClient {
    @PostMapping("/api/product/batch-get")
    List<ProductVO> getProductByIds(@RequestParam("productIds") List<Long> productIds);

    @PutMapping("/api/product/stock")
    String updateProductStock(@Valid @RequestBody OrderDetailDTO dto);

    @PostMapping("/api/product/page")
    PageData<ProductVO> getProductByPage(@Valid @RequestBody ProductPageQuery query);
}
