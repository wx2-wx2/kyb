package com.wx2.gateway.filters;

import com.wx2.common.exception.BizException;
import com.wx2.gateway.config.AuthProperties;
import com.wx2.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        boolean isExclude = isExclude(path);
        // 尝试从请求头获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }
        // 解析token
        Long userId = null;
        if (token != null) {
            try {
                userId = jwtTool.parseToken(token);
            } catch (BizException e) {
                // 只有非排除接口的token无效时才拦截
                if (!isExclude) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                // 排除接口的token无效则忽略，视为匿名用户
            }
        }
        // 处理非排除接口的登录校验
        if (!isExclude) {
            // 非排除接口必须有有效token
            if (userId == null) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        // 传递用户信息
        ServerWebExchange swe = exchange;
        if (userId != null) {
            // 创建一个final副本用于lambda表达式
            final Long finalUserId = userId;
            swe = exchange.mutate()
                    .request(builder -> builder.header("user-info", finalUserId.toString()))
                    .build();
        }
        // 放行
        return chain.filter(swe);
    }

    private boolean isExclude(String path) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
