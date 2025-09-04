package com.wx2.common.exception;

import com.wx2.common.error.BizError;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String message;

    /**
     * 构造方法（基于错误码枚举）
     */
    public BizException(BizError bizError) {
        this.code = bizError.getCode();
        this.message = bizError.getMessage();
    }

    /**
     * 构造方法（自定义错误信息）
     */
    public BizException(BizError bizError, String message) {
        this.code = bizError.getCode();
        this.message = message;
    }
}
