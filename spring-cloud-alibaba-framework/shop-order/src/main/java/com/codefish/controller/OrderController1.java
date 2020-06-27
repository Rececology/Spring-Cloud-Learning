package com.codefish.controller;

import com.alibaba.fastjson.JSON;
import com.codefish.domain.Order;
import com.codefish.domain.Product;
import com.codefish.service.OrderService;
import com.codefish.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @author John Fish
 * 1.模拟下单网络延迟，并使用 jmeter 进行 下单接口 压测
 * 2.访问 /order/message 发现服务明显变慢
 */
//@RestController
@Slf4j
public class OrderController1 {
    // 保存订单
    @Autowired
    private OrderService orderService;
    // Feign
    @Autowired
    private ProductService productService;

    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        log.info("接收到{}号商品的下单请求，接下来调用商品微服务查询此商品信息", pid);
        // 调用 service-procudt 微服务获取 product 信息
        // Feign 服务调用 配合接口十分简单 @EnableFeignClients + @FeignClient("service-product")
        Product product = productService.findById(pid);
        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product));

        // 模拟网络延迟
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order order = saveOrder(product);
        log.info("创建订单成功，订单信息为：{}", JSON.toJSONString(order));
        return order;
    }

    // 使用 jmeter 对下单服务进行压测，然后访问该服务
    @GetMapping("/order/message")
    private String Message() {
        return "高并发下的服务容错问题测试";
    }

    private Order saveOrder(Product product) {
        Order order = new Order();
        order.setUid(1);
        order.setUsername("test user");
        order.setPid(product.getPid());
        order.setPname(product.getPname());
        order.setPprice(product.getPprice());
        order.setNumber(1);
        orderService.save(order);
        return order;
    }

}
