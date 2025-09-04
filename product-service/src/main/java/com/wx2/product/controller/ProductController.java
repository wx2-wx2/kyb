package com.wx2.product.controller;

import com.wx2.common.model.PageData;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.common.model.query.IdQuery;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.product.model.query.*;
import com.wx2.common.model.vo.ProductVO;
import com.wx2.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    /**
     * 根据商品id查询商品
     */
    @GetMapping("/{productId}")
    public ProductVO getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    /**
     * 根据商品id批量查询商品
     */
    @PostMapping("/batch-get")
    public List<ProductVO> getProductByIds(@RequestParam("productIds") List<Long> productIds) {
        return productService.getProductByIds(productIds);
    }

    /**
     * 分页查询商品
     */
    @PostMapping("/page")
    public PageData<ProductVO> getProductByPage(@Valid @RequestBody ProductPageQuery query) {
        return productService.getProductByPage(query);
    }

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public String addProduct(@Valid @RequestBody ProductQuery query) {
        productService.addProduct(query);
        return "新增商品成功";
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public String updateProductById(@Valid @RequestBody ProductQuery query) {
        productService.updateProductById(query);
        return "更新商品成功";
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete")
    public String deleteProductById(@Valid @RequestBody IdQuery query) {
        productService.deleteProductById(query.getId());
        return "删除商品成功";
    }

    /**
     * 更新商品状态
     */
    @PutMapping("/status")
    public String updateProductStatus(@Valid @RequestBody StatusQuery query) {
        productService.updateProductStatus(query);
        return "商品状态更新成功";
    }

    /**
     * 更新商品库存
     */
    @PutMapping("/stock")
    public String updateProductStock(@Valid @RequestBody OrderDetailDTO dto) {
        productService.updateProductStock(dto);
        return "商品库存更新成功";
    }

    /**
     * 更新商品销量
     */
    @PutMapping("/sold")
    public String updateProductSold(@Valid @RequestBody ProductSoldQuery query) {
        productService.updateProductSold(query);
        return "商品销量更新成功";
    }
}
