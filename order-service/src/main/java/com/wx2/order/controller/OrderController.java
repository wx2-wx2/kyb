package com.wx2.order.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.order.model.query.OrderQuery;
import com.wx2.order.model.vo.OrderDetailVO;
import com.wx2.order.model.vo.OrderVO;
import com.wx2.order.service.OrderDetailService;
import com.wx2.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    /**
     * 根据订单id查询订单
     */
    @GetMapping("/{orderId}")
    public OrderVO getOrderById(@PathVariable Long orderId) {
        return BeanUtil.copyProperties(orderService.getById(orderId), OrderVO.class);
    }

    /**
     * 根据订单id查询订单详情
     */
    @GetMapping("/detail/{orderId}")
    public List<OrderDetailVO> getOrderDetailByOrderId(@PathVariable Long orderId) {
        return orderDetailService.getOrderDetailByOrderId(orderId);
    }

    /**
     * 创建订单
     */
    @SentinelResource(value = "addOrder", blockHandler = "addOrderFallback")
    @PostMapping("/add")
    public Long addOrder(@Valid @RequestBody OrderQuery query) {
        return orderService.addOrder(query);
    }

    public Long addOrderFallback(@Valid @RequestBody OrderQuery request, BlockException e) {
        log.error("addOrderFallback", e);
        return -1L;
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/status")
    public String updateOrderStatus(@Valid @RequestBody StatusQuery query) {
        orderService.updateStatusById(query);
        return "更新订单状态成功";
    }

}
