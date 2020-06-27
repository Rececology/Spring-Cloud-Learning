package com.codefish.dao;

import com.codefish.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author John Fish
 * JpaRepository args：实体类型，实体类 Id 类型（主键类型）
 */
public interface OrderDao extends JpaRepository<Order, Long> {
}
