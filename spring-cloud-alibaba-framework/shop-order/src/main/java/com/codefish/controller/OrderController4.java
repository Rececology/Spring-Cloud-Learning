package com.codefish.controller;

import com.alibaba.fastjson.JSON;
import com.codefish.domain.Order;
import com.codefish.domain.Product;
import com.codefish.service.OrderService;
import com.codefish.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author John Fish
 * 1.模拟下单网络延迟，并使用 jmeter 进行 下单接口 压测
 * 2.访问 /order/message 发现服务明显变慢
 *
 * 3.使用 rocketmq 下单后发送消息
 */
//@RestController
@Slf4j
public class OrderController4 {
    // 保存订单
    @Autowired
    private OrderService orderService;
    // Feign
    @Autowired
    private ProductService productService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        log.info("接收到{}号商品的下单请求，接下来调用商品微服务查询此商品信息", pid);

        Product product = productService.findById(pid);
        if (product == null) {
            Order order = new Order();
            order.setPname("下单失败");
            return order;
        }
        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product));


        Order order = saveOrder(product);
        log.info("创建订单成功，订单信息为：{}", JSON.toJSONString(order));
        //向 mq 投递一个消息
        // arg1：指定 topic arg2：指定消息体
        rocketMQTemplate.convertAndSend("order-topic", order);

        return order;
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
