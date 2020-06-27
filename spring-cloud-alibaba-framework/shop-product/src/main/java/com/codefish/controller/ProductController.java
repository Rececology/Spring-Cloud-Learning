package com.codefish.controller;

import com.alibaba.fastjson.JSON;
import com.codefish.domain.Product;
import com.codefish.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author John Fish
 */
@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //扣减库存
    @RequestMapping("/product/reduceInventory")
    public void reduceInventory(Integer pid, Integer number) {
        productService.reduceInventory(pid, number);
    }

    // 测试网关限流
    @RequestMapping("/product/api1/demo1")
    public String demo1() {
        return "demo";
    }

    @RequestMapping("/product/api1/demo2")
    public String demo2() {
        return "demo";
    }

    @RequestMapping("/product/api2/demo1")
    public String demo3() {
        return "demo";
    }

    @RequestMapping("/product/api2/demo2")
    public String demo4() {
        return "demo";
    }

    @GetMapping("product/{pid}")
    public Product product(@PathVariable("pid") Integer pid) {
        log.info("接下来要进行{}号商品信息的查询", pid);
        Product product = productService.findByPid(pid);
        log.info("查询到商品：" + JSON.toJSONString(product));
        return product;
    }
}
