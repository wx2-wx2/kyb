package com.wx2.common.constant;

public final class RedisConstant {
    // 用户模块缓存前缀
    public static final String USER_SIGN_PREFIX = "user:sign:";

    // 题目模块缓存前缀
    public static final String CACHE_QUESTION_PREFIX = "cache:question:";
    public static final String QUESTION_CLICK_IP_PREFIX = "click:question:ip:";
    public static final String MATH_HOT_RANK_PREFIX = "rank:math:hot:";
    public static final String ENGLISH_HOT_RANK_PREFIX = "rank:english:hot:";
    public static final String POLITICS_HOT_RANK_PREFIX = "rank:politics:hot:";
    public static final String QUESTION_LOCK_PREFIX = "lock:question:";

    // 商品模块缓存前缀
    public static final String CACHE_PRODUCT_PREFIX = "cache:product:";
    public static final String PRODUCT_LOCK_PREFIX = "lock:product:";

    // 院校模块缓存前缀
    public static final String CACHE_COLLEGE_PREFIX = "cache:college:";
    public static final String COLLEGE_LOCK_PREFIX = "lock:college:";

    // AI模块缓存前缀
    public static final String USER_CONVERSATIONS_PREFIX = "user:conversations:";
    public static final String CONVERSATION_MESSAGES_PREFIX = "conversation:messages:"; // 存储会话消息

    // 普通超时时长
    public static final long EXPIRATION_MINUTES = 30L;
    // 空对象超时时长
    public static final long NULL_EXPIRATION_MINUTES = 2L;
    // 用户点击冷却
    public static final long CLICK_COOLDOWN_SECONDS = 60;
    // 会话超时时长
    public static final long EXPIRATION_DAYS = 30;
}
