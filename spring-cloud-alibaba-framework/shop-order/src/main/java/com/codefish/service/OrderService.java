package com.codefish.service;

import com.codefish.domain.Order;

/**
 * @author John Fish
 */
public interface OrderService {
    // 创建订单
    void save(Order order);
}
