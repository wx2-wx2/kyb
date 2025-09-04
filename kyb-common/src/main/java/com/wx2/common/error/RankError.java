package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum RankError implements BizError {
    NOT_EXIST(7001, "榜单不存在");

    private final int code;
    private final String message;

    RankError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
