package com.wx2.cart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.cart.model.entity.Cart;
import com.wx2.common.model.query.CartNumQuery;
import com.wx2.cart.model.vo.CartVO;

import java.util.List;

public interface CartService extends IService<Cart> {
    /**
     * 查询用户购物车
     */
    List<CartVO> getUserCart();

    /**
     * 添加商品到购物车
     */
    void addToCart(CartNumQuery query);

    /**
     * 根据商品id批量删除商品
     */
    void removeByProductIds(List<Long> productIds);
}
