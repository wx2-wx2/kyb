package com.wx2.payment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wx2.payment.model.dto.PaymentDTO;
import com.wx2.payment.model.entity.Payment;
import com.wx2.payment.model.query.PaymentQuery;
import com.wx2.common.model.vo.PaymentVO;
import com.wx2.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 根据支付单id查询支付单
     */
    @GetMapping("/{paymentId}")
    public PaymentVO getPaymentById(@PathVariable Long paymentId) {
        return BeanUtil.copyProperties(paymentService.getById(paymentId), PaymentVO.class);
    }

    /**
     * 根据订单id查询支付单
     */
    @GetMapping("/by-order/{orderId}")
    public PaymentVO getPaymentByOrderId(@PathVariable Long orderId) {
        Payment payment = paymentService.lambdaQuery().eq(Payment::getOrderId, orderId).one();
        return BeanUtil.copyProperties(payment, PaymentVO.class);
    }

    /**
     * 新增支付单
     */
    @PostMapping("/add")
    public Long addPayment(@Valid @RequestBody PaymentQuery query) {
        return paymentService.addPayment(query);
    }

    /**
     * 支付
     */
    @PostMapping("/deal")
    public Long dealPayment(@Valid @RequestBody PaymentDTO dto) {
        return paymentService.dealPayment(dto);
    }
}
