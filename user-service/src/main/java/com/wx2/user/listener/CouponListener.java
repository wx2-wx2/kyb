package com.wx2.user.listener;

import com.wx2.user.service.CouponService;
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
public class CouponListener {

    private final CouponService couponService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = COUPON_QUEUE),
            exchange = @Exchange(name = COUPON_EXCHANGE, type = "topic"),
            key = COUPON_USE_KEY
    ))
    public void useCoupon(List<Long> couponIdList) {
        couponService.updateCouponStatusByIds(couponIdList, 1);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = COUPON_QUEUE),
            exchange = @Exchange(name = COUPON_EXCHANGE, type = "topic"),
            key = COUPON_RESET_KEY
    ))
    public void resetCoupon(List<Long> couponIdList) {
        couponService.updateCouponStatusByIds(couponIdList, 0);
    }

}
