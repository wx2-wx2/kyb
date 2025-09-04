package com.wx2.gateway.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.wx2.common.error.AuthError;
import com.wx2.common.exception.BizException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTool {
    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    public String createToken(Long userId, Duration ttl) {
        // 生成jwt
        return JWT.create()
                .setPayload("user", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    public Long parseToken(String token) {
        // 校验token是否为空
        if (token == null) {
            throw new BizException(AuthError.NOT_LOGGED_IN);
        }
        // 校验并解析jwt
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new BizException(AuthError.TOKEN_INVALID, e.getMessage());
        }
        // 校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new BizException(AuthError.TOKEN_INVALID);
        }
        // 校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new BizException(AuthError.TOKEN_EXPIRED);
        }
        // 数据格式校验
        Object userPayload = jwt.getPayload("user");
        if (userPayload == null) {
            // 数据为空
            throw new BizException(AuthError.TOKEN_INVALID);
        }

        // 数据解析
        try {
            return Long.valueOf(userPayload.toString());
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new BizException(AuthError.TOKEN_INVALID);
        }
    }
}
