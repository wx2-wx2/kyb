package com.wx2.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.api.client.UserFeignClient;
import com.wx2.common.error.PaymentError;
import com.wx2.common.error.UserError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.common.utils.UserContext;
import com.wx2.payment.enums.PaymentStatus;
import com.wx2.payment.enums.PaymentType;
import com.wx2.payment.mapper.PaymentMapper;
import com.wx2.payment.model.dto.PaymentDTO;
import com.wx2.payment.model.entity.Payment;
import com.wx2.payment.model.query.PaymentQuery;
import com.wx2.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.wx2.common.constant.MqConstant.PAY_EXCHANGE;
import static com.wx2.common.constant.MqConstant.PAY_SUCCESS_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    private final UserFeignClient userFeignClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Long addPayment(PaymentQuery query) {
        // 判断支付类型
        if (!Objects.equals(query.getType(), PaymentType.BALANCE.getCode())) {
            throw new BizException(PaymentError.TYPE_INVALID);
        }
        // 查询支付单
        Payment oldPayment = lambdaQuery().eq(Payment::getOrderId, query.getOrderId()).one();
        // 不存在则新增
        if (ObjectUtil.isNull(oldPayment)) {
            Payment payment = BeanUtil.copyProperties(query, Payment.class);
            payment.setUserId(UserContext.getUserId());
            save(payment);
            return payment.getId();
        }
        // 判断旧订单状态
        checkPaymentStatus(oldPayment);
        // 更新支付类型
        if (!ObjectUtil.equals(oldPayment.getType(), query.getType())) {
            Payment payment = BeanUtil.copyProperties(query, Payment.class);
            payment.setUserId(UserContext.getUserId());
            updateById(payment);
            return payment.getId();
        }

        return oldPayment.getId();
    }


    @Override
    @Transactional
    public Long dealPayment(PaymentDTO dto) {
        // 查询支付单
        Payment payment = getById(dto.getId());
        // 检验支付单状态
        checkPaymentStatus(payment);
        // 扣减余额
        UserBalanceQuery userBalanceQuery = new UserBalanceQuery();
        userBalanceQuery.setUserId(UserContext.getUserId());
        userBalanceQuery.setAmount(payment.getAmount().negate());
        userBalanceQuery.setPassword(dto.getPassword());
        try {
            userFeignClient.deductBalance(userBalanceQuery);
        } catch (Exception e) {
            throw new BizException(UserError.PASSWORD_ERROR_OR_BALANCE_INSUFFICIENT);
        }
        // 修改订单状态
        boolean success = lambdaUpdate()
                .set(Payment::getStatus, PaymentStatus.SUCCESS.getCode())
                .set(Payment::getPayTime, LocalDateTime.now())
                .eq(Payment::getId, dto.getId())
                .eq(Payment::getStatus, PaymentStatus.UNPAID.getCode())
                .update();
        // 不成功则抛出异常
        if (!success) {
            throw new BizException(PaymentError.FAIL_PAY);
        }
        // 向消息队列发送修改订单状态消息
        try {
            rabbitTemplate.convertAndSend(PAY_EXCHANGE, PAY_SUCCESS_KEY, payment.getOrderId());
        } catch (Exception e) {
            log.error("修改订单状态消息异常");
        }

        return payment.getId();
    }

    private static void checkPaymentStatus(Payment oldPayment) {
        if (ObjectUtil.equals(oldPayment.getStatus(), PaymentStatus.PAYING.getCode())) {
            throw new BizException(PaymentError.PAYING);
        }
        if (ObjectUtil.equals(oldPayment.getStatus(), PaymentStatus.SUCCESS.getCode())) {
            throw new BizException(PaymentError.ALREADY_PAY);
        }
        if (ObjectUtil.equals(oldPayment.getStatus(), PaymentStatus.FAILED.getCode())) {
            throw new BizException(PaymentError.FAIL_PAY);
        }
    }
}
