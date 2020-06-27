package com.codefish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author John Fish
 */
@SpringBootApplication
public class RocketMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplication.class, args);
    }

//    @Bean
//    public RocketMQTemplate getRestTemplate() {
//        return new RocketMQTemplate();
//    }
}
