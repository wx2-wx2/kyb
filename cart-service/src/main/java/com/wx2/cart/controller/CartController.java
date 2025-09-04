package com.wx2.cart.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wx2.cart.model.entity.Cart;
import com.wx2.common.model.query.CartNumQuery;
import com.wx2.cart.model.query.CartQuery;
import com.wx2.cart.model.vo.CartVO;
import com.wx2.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 查询用户购物车
     */
    @GetMapping("/get")
    public List<CartVO> getUserCart() {
        return cartService.getUserCart();
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public String addToCart(@Valid @RequestBody CartNumQuery query) {
        cartService.addToCart(query);
        return "添加商品到购物车成功";
    }

    /**
     * 更新购物车
     */
    @PutMapping("update")
    public String updateCart(@Valid @RequestBody CartQuery request) {
        cartService.updateById(BeanUtil.copyProperties(request, Cart.class));
        return "更新购物车成功";
    }

    /**
     * 批量删除购物车中商品
     */
    @DeleteMapping("/delete/item")
    public String deleteFromCart(@RequestParam("cartIds") List<Long> cartIds) {
        cartService.removeByIds(cartIds);
        return "删除商品成功";
    }

    /**
     * 根据商品id批量删除购物车中商品
     */
    @DeleteMapping("/delete/product")
    public String deleteFromCartByProductIds(@RequestParam("productIds") List<Long> productIds) {
        cartService.removeByProductIds(productIds);
        return "删除商品成功";
    }
}
