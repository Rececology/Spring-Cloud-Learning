package com.codefish.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author John Fish
 */
@Service
@Slf4j
public class SentinelMessageServiceImpl{
    // @SentinelResource 的使用：用于定义资源 | 并提供可选的异常处理和 fallback 配置项
    //定义一个资源
    //定义当资源内部发生异常的时候的处理逻辑
    //blockHandler  定义当资源内部发生了BlockException应该进入的方法[捕获的是Sentinel定义的异常]
    //fallback      定义当资源内部发生了Throwable应该进入的方法
    @SentinelResource(
            value = "message",
            blockHandlerClass = OrderServiceImpl3BlockHandler.class,
            fallbackClass = OrderServiceImpl3Fallback.class
            // 上下基本相同 下面的可以直接在该类中定义响应函数
//            blockHandler = "blockHandler",
//            fallback = "fallback"
    )
    public void message() {
        System.out.println("message");
    }

}
