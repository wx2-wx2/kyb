package com.wx2.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {
    BOOK(1, "书籍"),
    REAL_QUESTION(2, "真题"),
    ONLINE_COURSE(3, "在线课程"),
    CONSULTING_SERVICE(4, "咨询服务");

    private final Integer code;
    private final String desc;

}
