package com.wx2.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx2.cart.model.entity.Cart;
import org.apache.ibatis.annotations.Param;

public interface CartMapper extends BaseMapper<Cart> {
    /**
     * 更新购物车中商品数量
     */
    void updateNum(@Param("productId") Long productId,
                   @Param("userId") Long userId,
                   @Param("num") Integer num);
}
