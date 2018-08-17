package com.yunzhijia.appdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.yunzhijia.appdemo.mapper") //扫描的是mapper.xml中namespace指向值的包位置
@SpringBootApplication
public class AppdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppdemoApplication.class, args);
    }
}
