package com.codefish.service.impl;

import com.alibaba.fastjson.JSON;
import com.codefish.dao.ProcuctDao;
import com.codefish.domain.Product;
import com.codefish.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author John Fish
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProcuctDao procuctDao;

    @Override
    public Product findByPid(Integer pid) {
        return procuctDao.findById(pid).get();
    }

    @Override
    public void reduceInventory(Integer pid, Integer number) {
        //查询
        Product product = procuctDao.findById(pid).get();
        System.out.println(JSON.toJSONString(product));
        //省略校验
        //内存中扣减
        product.setStock(product.getStock() - number);
        //模拟异常
//        int i = 1 / 0;
        //保存
        procuctDao.save(product);
    }
}
