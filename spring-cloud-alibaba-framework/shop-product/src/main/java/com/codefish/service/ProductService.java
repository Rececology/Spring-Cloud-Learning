package com.codefish.service;

import com.codefish.domain.Product;

/**
 * @author John Fish
 */
public interface ProductService {
    //根据pid查询商品信息
    Product findByPid(Integer pid);

    //扣减库存
    void reduceInventory(Integer pid, Integer number);
}
