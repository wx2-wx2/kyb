package com.wx2.cart.listener;

import com.wx2.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wx2.common.constant.MqConstant.*;

@Component
@RequiredArgsConstructor
public class CartListener {

    private final CartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = CART_QUEUE),
            exchange = @Exchange(name = CART_EXCHANGE, type = "topic"),
            key = CART_CLEAR_KEY
    ))
    public void clearCart(List<Long> productIdList) {
        cartService.removeByProductIds(productIdList);
    }

}
