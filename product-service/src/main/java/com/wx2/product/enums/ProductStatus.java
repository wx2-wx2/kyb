package com.wx2.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    NORMAL(1, "正常"),
    OFF_SHELF(2, "下架"),
    DELETED(3, "删除");

    private final Integer code;
    private final String desc;

}
