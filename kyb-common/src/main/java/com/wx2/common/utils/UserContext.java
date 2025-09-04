package com.wx2.common.utils;

/**
 * 用户上下文工具类，用于存储和获取当前登录用户信息
 */
public class UserContext {
    /**
     * 用ThreadLocal存储当前线程的用户id，确保线程隔离
     */
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 保存当前登录用户id到ThreadLocal
     */
    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取当前登录用户id
     */
    public static Long getUserId() {
        return threadLocal.get();
    }

    /**
     * 移除当前登录用户id
     */
    public static void clear() {
        threadLocal.remove();
    }
}
