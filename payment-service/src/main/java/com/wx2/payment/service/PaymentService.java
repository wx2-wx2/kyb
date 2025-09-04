package com.wx2.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.payment.model.dto.PaymentDTO;
import com.wx2.payment.model.entity.Payment;
import com.wx2.payment.model.query.PaymentQuery;

public interface PaymentService extends IService<Payment> {
    /**
     * 新增支付单
     */
    Long addPayment(PaymentQuery query);

    /**
     * 支付
     */
    Long dealPayment(PaymentDTO dto);
}
