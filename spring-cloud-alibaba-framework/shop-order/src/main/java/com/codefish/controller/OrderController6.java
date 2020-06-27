package com.codefish.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author John Fish
 * 1.nacos 服务注册与发现
 * 2.ribbon 负载均衡
 * 3.feign 远程调用 默认集成 ribbon
 */
//@RestController
@Slf4j
public class OrderController6 {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/rocketmq")
    public void order() {
        String body = "body";
        rocketMQTemplate.convertAndSend("order-topic", body);
    }

}
