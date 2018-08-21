package com.yunzhijia.appdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Timer;
import java.util.TimerTask;

@EnableTransactionManagement
@MapperScan("com.yunzhijia.appdemo.mapper") //扫描的是mapper.xml中namespace指向值的包位置
@SpringBootApplication
@EnableCaching
public class AppdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppdemoApplication.class, args);
        myTimer();
    }

    public static void myTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println("------定时任务--------");
            }
        }, 0, 1000);
    }
}
