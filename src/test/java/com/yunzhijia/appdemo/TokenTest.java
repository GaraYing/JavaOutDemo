package com.yunzhijia.appdemo;

import com.yunzhijia.appdemo.redis.RedisDao;
import com.yunzhijia.appdemo.redis.TokenScan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.*;

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
//        tokenScan.run();
        tokenScan.setAppId(appId);
        tokenScan.setScope("app");
        new Thread(tokenScan).start();

//        System.out.println(redisDao.get("accessToken"));

    }

    @Autowired
    private RedisDao redisDao;

    @Test
    public void testRedisConn(){
//        redisDao.set("str","string");
        List<String> list = new LinkedList<>();
        list.add("12");
        list.add("34");
        list.add("23");
        list.add("23");
        System.out.println(list);
        redisDao.set("str2",list);

        Map<String , Object> map =  new HashMap<>();
        map.put("name","网三");
        map.put("age","23");
        redisDao.set("str3",map);

        System.out.println(redisDao.get("accessToken"));
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }
}
