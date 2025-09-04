package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum FeignClientError implements BizError{
    FEIGN_CLIENT_ERROR(8001, "feign客户端调用失败");

    private final int code;
    private final String message;

    FeignClientError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
