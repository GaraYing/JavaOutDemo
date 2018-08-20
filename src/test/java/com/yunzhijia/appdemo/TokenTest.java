package com.yunzhijia.appdemo;

import com.yunzhijia.appdemo.redis.TokenScan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @description: Accesstoken测试
 * @author: GaraYing
 * @create: 2018-08-20 15:14
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TokenTest {

    @Autowired
    private TokenScan tokenScan;

//    @Resource
    String appId = "500046581";
//    @Resource
    String scope = "app";

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @Test
    public void testTokenRedis(){
//        new TokenScan(appId,scope).run();
//        new Thread(new TokenScan(appId,scope)).start();
        tokenScan.run();
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }
}
