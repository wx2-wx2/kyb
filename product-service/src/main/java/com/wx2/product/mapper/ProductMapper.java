package com.wx2.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx2.product.model.entity.Product;
import com.wx2.common.model.query.ProductPageQuery;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据查询条件统计总记录数
     */
    long countByQuery(ProductPageQuery query);

    /**
     * 根据查询条件分页查询商品
     */
    List<Product> selectByQuery(ProductPageQuery query);
}
