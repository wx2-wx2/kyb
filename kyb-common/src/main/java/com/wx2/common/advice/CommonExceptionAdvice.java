package com.wx2.common.advice;

import com.wx2.common.exception.BizException;
import com.wx2.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {

    @ExceptionHandler(BizException.class)
    public Object handleBizException(BizException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.fail("业务异常 -> " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + "：" + fieldError.getDefaultMessage())
                .collect(Collectors.joining("；"));
        log.error("参数校验异常：{}", errorMsg);
        return Result.fail("参数校验异常 -> " + errorMsg);
    }

    @ExceptionHandler(DataAccessException.class)
    public Object handleDataAccessException(DataAccessException e) {
        String errorMsg;
        if (e.getMessage().contains("Duplicate entry")) {
            errorMsg = "数据已存在，请勿重复提交";
        } else if (e.getMessage().contains("SQL syntax")) {
            errorMsg = "数据操作语法错误";
        } else if (e.getMessage().contains("connection")) {
            errorMsg = "数据库连接异常，请稍后重试";
        } else {
            errorMsg = "数据库操作失败";
        }
        log.error("数据库异常：{}", e.getMessage());
        return Result.fail("数据库异常 -> " + errorMsg);
    }

}
