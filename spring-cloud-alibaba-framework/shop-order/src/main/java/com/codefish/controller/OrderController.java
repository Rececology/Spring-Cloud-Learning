package com.codefish.controller;

import com.alibaba.fastjson.JSON;
import com.codefish.domain.Order;
import com.codefish.domain.Product;
import com.codefish.service.OrderService;
import com.codefish.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author John Fish
 * 1.nacos 服务注册与发现
 * 2.ribbon 负载均衡
 * 3.feign 远程调用 默认集成 ribbon
 */
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DiscoveryClient discoveryClient;
    // 4.Feign
    @Autowired
    private ProductService productService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid) {
        log.info("接收到{}号商品的下单请求，接下来调用商品微服务查询此商品信息", pid);
        // 调用 service-procudt 微服务获取 product 信息
        // 1.使用 拼接写死
//        Product product1 = restTemplate.getForObject(
//                "http://localhost:8081/product/" + pid, Product.class);
//        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product1));

        // 2.nacos 服务中心取 (需要服务名) 只是取得了 ip + port，还是需要拼接
//        ServiceInstance serviceInstance = discoveryClient.getInstances("service-product").get(0);
//        String url = serviceInstance.getHost() + ":" + serviceInstance.getPort();
//        log.info("从nacos获取微服务地址：" + url);
//        url = "http://" + url + "/product/" + pid;
//        System.out.println(url);
//        Product product2 = restTemplate.getForObject(url, Product.class);
//        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product2));

        // 3.Ribbon 负载均衡 直接用 服务名 替换 ip + port | 依赖 + 注解（RestT 上 @LoadBanlanced)
//        String url2 = "service-product";
//        Product product3 = restTemplate.getForObject("http://" + url2 + "/product/" + pid, Product.class);
//        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product3));

        // 4.Feign 服务调用 配合接口十分简单 @EnableFeignClients + @FeignClient("service-product")
        Product product4 = productService.findById(pid);
        log.info("查询到{}号商品的信息，内容是：{}", pid, JSON.toJSONString(product4));

        Order order = saveOrder(product4);

        rocketMQTemplate.convertAndSend("order-topic", order);

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        rocketMQTemplate.sendOneWay("order-topic", order);

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
