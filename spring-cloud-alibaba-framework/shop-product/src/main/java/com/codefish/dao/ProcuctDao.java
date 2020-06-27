package com.codefish.dao;

import com.codefish.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author John Fish
 */
public interface ProcuctDao extends JpaRepository<Product, Integer> {
}
