package com.wx2.common.error;

import lombok.Getter;

@Getter
public enum QuestionError implements BizError{
    QUESTION_NOT_EXIST(2001, "题目不存在"),
    KNOWLEDGE_NOT_EXIST(2002, "知识点不存在"),
    KNOWLEDGE_ALREADY_EXIST(2003, "知识点已存在");

    private final int code;
    private final String message;

    QuestionError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
