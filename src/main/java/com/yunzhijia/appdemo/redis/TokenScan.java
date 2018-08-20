package com.yunzhijia.appdemo.redis;

import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.auth.GatewayAuth2;
import com.yunzhijia.appdemo.util.HttpClientHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: accessToken生成线程类
 * @author: GaraYing
 * @create: 2018-08-20 14:37
 **/

@Component
public class TokenScan implements Runnable {

    private String appId;
    private String scope;
    @Autowired
    private RedisDao redisDao;
    @Value("${APP.SECRET}")
    private String secret;
    @Value("${YUNZHIJIA.GATEWAY.HOST}")
    private String gatewayHost;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public RedisDao getRedisDao() {
        return redisDao;
    }

    public void setRedisDao(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    public TokenScan() {
    }

    public TokenScan(String appId, String scope) {
        this.appId = appId;
        this.scope = scope;
    }

    public boolean run = true;// 线程开关
    private static final int cycle = 45;//刷新周期，45min刷新一次

    @Override
    public void run() {
        while (run) {
            String url = gatewayHost.concat("/oauth2/token/getAccessToken");
            String timestamp = String.valueOf(System.currentTimeMillis());
            Map<String, String> parm = new HashMap<String, String>(5);
//            String scope = "app";
            parm.put("scope", scope);
            parm.put("timestamp", timestamp);
            parm.put("appId", appId);
            parm.put("secret", secret);
            JSONObject result = null;
            try {
                result = JSONObject.parseObject(GatewayAuth2.gatewayRequestJson(url, JSONObject.toJSONString(parm))).getJSONObject("data");
                if (result.getString("accessToken") != null) {
                    String accessToken = result.getString("accessToken");
                    redisDao.addString("accessToken", accessToken);
                    System.out.println("accessToken-=-=-=-=" + accessToken);
                }
                Thread.sleep(cycle * 60 * 1000);//3600000
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
