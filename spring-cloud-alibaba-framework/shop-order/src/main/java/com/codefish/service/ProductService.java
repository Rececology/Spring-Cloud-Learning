package com.codefish.service;

import com.codefish.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author John Fish
 */
@FeignClient(value = "service-product")
//        fallback = ProductServiceFallback.class,
//        fallbackFactory = ProductServiceFallbackFactory.class)
public interface ProductService {
    @GetMapping("/product/{pid}")
    Product findById(@PathVariable("pid") Integer pid);

    @GetMapping("/product/reduceInventory")
    void reduceInventory(@RequestParam("pid") Integer pid,
                         @RequestParam("number") Integer number);
}
