package com.wx2.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class KybLogAspect {

    @Pointcut("@annotation(KybLog)")
    public void kybLogPointcut() {}

//    @Before("kybLogPointcut()")
//    public void before(JoinPoint joinPoint) {
//        System.out.println("方法执行前");
//    }
//
//    @After("kybLogPointcut()")
//    public void after(JoinPoint joinPoint) {
//        System.out.println("方法执行后");
//    }
//
//    @AfterReturning(value = "kybLogPointcut()", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, String result) {
//        System.out.println("方法返回结果后");
//    }
//
//    @AfterThrowing(value = "kybLogPointcut()", throwing = "exception")
//    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
//        System.out.println("进入异常");
//    }


    @Around("kybLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String startTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getMethod().getName();

        log.info("===== 方法执行日志开始 =====");
        log.info("开始执行时间: {}", startTimeStr);
        log.info("执行类: {}", className);
        log.info("执行方法: {}", methodName);

        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        Map<String, Object> paramMap = new HashMap<>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
            log.info("请求参数: {}", paramMap);
        } else {
            log.info("该方法无请求参数");
        }

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("方法执行成功");

            if (result != null) {
                log.info("返回结果: {}", result);
            } else {
                log.info("该方法无返回结果");
            }

            return result;
        } catch (Exception e) {
            log.error("方法执行异常: {}", e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("方法执行结束，总耗时: {}ms", executionTime);
            log.info("===== 方法执行日志结束 =====");
        }
    }
}
