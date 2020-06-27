package com.codefish.service.impl;

import com.alibaba.fastjson.JSON;
import com.codefish.dao.OrderDao;
import com.codefish.domain.Order;
import com.codefish.domain.Product;
import com.codefish.service.ProductService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author John Fish
 */
@Service
@Slf4j
public class OrderServiceImpl5{

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductService productService;


    @GlobalTransactional//全局事务控制
    public Order createOrder(Integer pid) {
        log.info("接收到{}号商品的下单请求,接下来调用商品微服务查询此商品信息", pid);

        //1 调用商品微服务,查询商品信息
        Product product = productService.findById(pid);
        log.info("查询到{}号商品的信息,内容是:{}", pid, JSON.toJSONString(product));

        // 保存订单
        Order order = saveOrder(product);
        log.info("创建订单成功，订单信息为：{}", JSON.toJSONString(order));

        //3 扣库存m
        productService.reduceInventory(pid, order.getNumber());

        //4 向mq中投递一个下单成功的消息

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
        orderDao.save(order);
        return order;
    }

}
