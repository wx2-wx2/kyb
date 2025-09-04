package com.wx2.common.advice;

import com.alibaba.fastjson2.JSON;
import com.wx2.common.model.Result;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.wx2")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除已经是Result类型的返回值，避免重复包装
        return !returnType.getParameterType().isAssignableFrom(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 判断是否为Feign调用（通过自定义请求头）
        if (request.getHeaders().containsKey("X-Request-Source") &&
                "feign".equals(request.getHeaders().getFirst("X-Request-Source"))) {
            return body;
        }

        Result<?> result = Result.success(body);
        if (body instanceof String) {
            return JSON.toJSONString(Result.success(body));
        }
        return result;
    }
}
