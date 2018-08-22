package com.yunzhijia.appdemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;

/**
 * @description: 线程任务执行类
 * @author: GaraYing
 * @create: 2018-08-22 10:55
 **/

public class TaskRunner implements ApplicationRunner,Order {

//    @Autowired
//    private TokenScan tokenScan;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        tokenScan.setAppId(appId);
//        tokenScan.setScope("app");
//        new Thread(tokenScan).start();
    }

    @Override
    public int value() {
        return 2;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
