package com.wx2.cart.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.api.client.ProductFeignClient;
import com.wx2.cart.constant.CartConstant;
import com.wx2.cart.mapper.CartMapper;
import com.wx2.cart.model.entity.Cart;
import com.wx2.common.model.query.CartNumQuery;
import com.wx2.cart.model.vo.CartVO;
import com.wx2.cart.service.CartService;
import com.wx2.common.error.CartError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.vo.ProductVO;
import com.wx2.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    private final CartMapper cartMapper;
    private final ProductFeignClient productFeignClient;


    @Override
    public List<CartVO> getUserCart() {
        // 查询用户购物车
        Long userId = UserContext.getUserId();
        List<Cart> cartList = lambdaQuery().eq(Cart::getUserId, userId).list();
        // 如果购物车列表为空，则返回空列表
        if (CollUtil.isEmpty(cartList)) {
            return List.of();
        }

        List<CartVO> voList = BeanUtil.copyToList(cartList, CartVO.class);
        // 查询商品最新信息
        getProductLatestInfo(voList);

        return voList;
    }

    @Override
    @Transactional
    public void addToCart(CartNumQuery query) {
        // 查询商品是否已存在用户购物车中
        Long userId = UserContext.getUserId();
        Long productId = query.getProductId();

        long count = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, productId)
                .count();
        // 存在则直接更新商品数量
        if (count > 0) {
            cartMapper.updateNum(query.getProductId(), userId, query.getNum());
            return;
        }
        // 不存在则检验用户购物车容量是否已达上限
        long itemCount = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .count();

        if (itemCount >= CartConstant.cartItemMax) {
            throw new BizException(CartError.REACH_MAX);
        }
        // 未达上限则新增购物车数据
        Cart cart = BeanUtil.copyProperties(query, Cart.class);
        cart.setUserId(userId);
        save(cart);
    }

    @Override
    @Transactional
    public void removeByProductIds(List<Long> productIds) {
        // 删除用户购物车中商品
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUserId())
                .in(Cart::getProductId, productIds);
        remove(queryWrapper);
    }

    private void getProductLatestInfo(List<CartVO> voList) {
        // 获取商品id列表
        List<Long> productIds = voList.stream()
                .map(CartVO::getProductId)
                .collect(Collectors.toList());
        // 调用商品服务查询最新信息
        List<ProductVO> productSet = productFeignClient.getProductByIds(productIds);
        // 如果没有查询到商品信息，则直接返回
        if (CollUtil.isEmpty(productSet)) {
            return;
        }
        // 遍历购物车列表，更新商品最新信息
        Map<Long, ProductVO> productMap = productSet.stream()
                .collect(Collectors.toMap(ProductVO::getId, Function.identity()));

        for (CartVO vo : voList) {
            ProductVO product = productMap.get(vo.getProductId());
            if (ObjectUtil.isNull(product)) {
                continue;
            }
            vo.setName(product.getName());
            vo.setImage(product.getImage());
            vo.setPrice(product.getPrice());
        }
    }
}
