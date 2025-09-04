package com.wx2.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.common.model.PageData;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.product.model.entity.Product;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.vo.ProductVO;
import com.wx2.product.model.query.ProductQuery;
import com.wx2.product.model.query.ProductSoldQuery;

import java.util.List;

public interface ProductService extends IService<Product> {
    /**
     * 根据商品id查询商品
     */
    ProductVO getProductById(Long productId);
    /**
     * 根据商品id批量查询商品
     */
    List<ProductVO> getProductByIds(List<Long> productIds);

    /**
     * 分页查询商品
     */
    PageData<ProductVO> getProductByPage(ProductPageQuery query);

    /**
     * 新增商品
     */
    void addProduct(ProductQuery query);

    /**
     * 更新商品
     */
    void updateProductById(ProductQuery query);

    /**
     * 删除商品
     */
    void deleteProductById(Long productId);

    /**
     * 更新商品状态
     */
    void updateProductStatus(StatusQuery query);

    /**
     * 更新商品库存
     */
    void updateProductStock(OrderDetailDTO dto);

    /**
     * 更新商品销量
     */
    void updateProductSold(ProductSoldQuery query);
}
