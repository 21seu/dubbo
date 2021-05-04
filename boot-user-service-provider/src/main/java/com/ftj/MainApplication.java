package com.ftj;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  1、导入dubbo-starter
 *  2、导入dubbo的其他依赖
 *  3、配置dubbo
 */
@EnableDubbo //开启基于注解的dubbo功能
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
