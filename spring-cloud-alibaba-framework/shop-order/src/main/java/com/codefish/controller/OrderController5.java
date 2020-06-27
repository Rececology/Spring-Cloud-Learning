package com.codefish.controller;

import com.codefish.domain.Order;
import com.codefish.service.impl.OrderServiceImpl5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author John Fish
 * 1.模拟下单网络延迟，并使用 jmeter 进行 下单接口 压测
 * 2.访问 /order/message 发现服务明显变慢
 */
//@RestController
@Slf4j
public class OrderController5 {

    @Autowired
    private OrderServiceImpl5 orderService;

    @RequestMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        return orderService.createOrder(pid);
    }


}
