package com.codefish.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.codefish.service.impl.SentinelMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author John Fish
 * sentinal
 * 1.流控
 *  1.1通过两个网址访问一个资源以使 sentinal 察觉到 资源 message，从而可以对 message 做链路流控
 *  1.2.需要 2.1.1 config a.yaml
 * 2.降级：备用方案
 * 3.热点规则：将规则具体到参数上
 */
//@RestController
@Slf4j
public class OrderController3 {

    @Autowired
    SentinelMessageServiceImpl sentinelMessageServiceImpl;

    // 降级 平均响应时间；异常比例；异常数
    int i = 0;
    @GetMapping("/order/message0")
    public String message0() {
        i++;
        if (i % 3 == 0) {
            throw new RuntimeException();
        }
        return "message";
    }

    // 流控
    @RequestMapping("/order/message1")
    public String message1() {
        sentinelMessageServiceImpl.message();
        return "message1";
    }

    @RequestMapping("/order/message2")
    public String message2() {
        sentinelMessageServiceImpl.message();
        return "message2";
    }

    //热点规则
    // 允许将规则具体到参数上
    @GetMapping("/order/message3")
    @SentinelResource
    public String message3(String name, Integer age) {
        return name + age;
    }


    // 允许将规则具体到参数上
    @GetMapping("/order/message4")
    @SentinelResource
    public String message4() {
        return "message4";
    }

}
