package com.codefish.service.impl;

import com.codefish.dao.OrderDao;
import com.codefish.dao.TxLogDao;
import com.codefish.domain.Order;
import com.codefish.domain.TxLog;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author John Fish
 */
@Service
public class OrderServiceImplRocketMQ {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TxLogDao txLogDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    public void createOrderBefore(Order order) {

        String txId = UUID.randomUUID().toString();

        //1.发送半事务消息
        rocketMQTemplate.sendMessageInTransaction(
                "tx_producer_group",
                "tx_topic",
                MessageBuilder.withPayload(order).setHeader("txId", txId).build(),
                order
        );
    }

    // 被调用
    @Transactional
    public void createOrder(String txId, Order order) {
        //保存订单
        orderDao.save(order);

        TxLog txLog = new TxLog();
        txLog.setTxId(txId);
        txLog.setDate(new Date());

        //记录事物日志
        txLogDao.save(txLog);
    }
}
