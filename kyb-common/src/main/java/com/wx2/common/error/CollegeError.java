package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum CollegeError implements BizError{
    NOT_EXIST(3001, "院校不存在");

    private final int code;
    private final String message;

    CollegeError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
