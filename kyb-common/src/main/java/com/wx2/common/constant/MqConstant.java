package com.wx2.common.constant;

public final class MqConstant {
    // 订单业务
    public static final String ORDER_EXCHANGE = "order.topic";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_DELAY_KEY = "order.delay.query";

    // 支付业务
    public static final String PAY_EXCHANGE = "pay.topic";
    public static final String PAY_QUEUE = "pay.queue";
    public static final String PAY_SUCCESS_KEY = "pay.success";

    // 购物车清理业务
    public static final String CART_EXCHANGE = "cart.topic";
    public static final String CART_QUEUE = "cart.queue";
    public static final String CART_CLEAR_KEY = "cart.clear";

    // 优惠券使用业务
    public static final String COUPON_EXCHANGE = "coupon.topic";
    public static final String COUPON_QUEUE = "coupon.queue";
    public static final String COUPON_USE_KEY = "coupon.use";
    public static final String COUPON_RESET_KEY = "coupon.reset";

    // 收藏业务
    public static final String COLLECTION_EXCHANGE = "collection.topic";
    public static final String COLLECTION_COLLEGE_QUEUE = "collection.college.queue";
    public static final String COLLECTION_COLLEGE_ADD_KEY = "collection.college.add";
    public static final String COLLECTION_COLLEGE_REMOVE_KEY = "collection.college.remove";

    // ES数据同步业务
    public static final String ES_EXCHANGE = "es.topic";

    // ES商品模块
    public static final String ES_PRODUCT_UPDATE_QUEUE = "es.product.update.queue";
    public static final String ES_PRODUCT_DELETE_QUEUE = "es.product.delete.queue";
    public static final String ES_PRODUCT_UPDATE_KEY = "es.product.update";
    public static final String ES_PRODUCT_DELETE_KEY = "es.product.delete";

    // ES题目模块
    public static final String ES_QUESTION_UPDATE_QUEUE = "es.question.update.queue";
    public static final String ES_QUESTION_DELETE_QUEUE = "es.question.delete.queue";
    public static final String ES_QUESTION_UPDATE_KEY = "es.question.update";
    public static final String ES_QUESTION_DELETE_KEY = "es.question.delete";
}
