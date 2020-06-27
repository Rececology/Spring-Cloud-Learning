package com.codefish.controller;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author John Fish
 */
@RestController
public class ProduceController {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private DefaultMQProducer producer;

    @GetMapping("/hello")
    public String send() {
        String str = "发送测试消息";
        rocketMQTemplate.convertAndSend("test-demo:111", str);
//        try {
//            Message msg = new Message("test-demo","111", UUID.randomUUID().toString()
//                    , str.getBytes("utf-8") );
//            SendResult sendResult = producer.send(msg);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        return "hello";
    }




    private void sendMsgToMq() throws Exception {
        String str = "发送测试消息";
        Message msg = new Message("test-demo","111", UUID.randomUUID().toString()
                , str.getBytes("utf-8") );
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
//        try {
//            msg = new Message("test-demo"
//                    , "111"
//                    , UUID.randomUUID().toString()
//                    , str.getBytes("utf-8"));
//            SendResult result = producer.send(msg);
//            if (result.getSendStatus() == SendStatus.SEND_OK) {
//                System.out.println("消息发送成功");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
