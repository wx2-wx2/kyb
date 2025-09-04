package com.wx2.order.handler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.List;

public class ListLongTypeHandler extends JacksonTypeHandler {
    public ListLongTypeHandler() {
        super(List.class);
    }
}
