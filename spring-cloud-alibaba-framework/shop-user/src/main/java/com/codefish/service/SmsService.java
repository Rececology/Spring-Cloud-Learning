package com.codefish.service;

import com.alibaba.fastjson.JSON;
import com.codefish.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author John Fish
 */
@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "shop-user", topic = "order-topic")
public class SmsService implements RocketMQListener<Order> {
    @Override
    public void onMessage(Order order) {
        log.info("接收到了一个订单信息{},接下来就可以发送短信通知了", JSON.toJSONString(order));
    }

//    @Override
//    public void onMessage(String message) {
//        log.info("接收到了一个订单信息{},接下来就可以发送短信通知了", message);
//    }
}
