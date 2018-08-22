package com.yunzhijia.appdemo.task;

import com.alibaba.fastjson.JSONObject;
import com.yunzhijia.appdemo.auth.GatewayAuth2;
import com.yunzhijia.appdemo.redis.RedisDao;
import com.yunzhijia.appdemo.redis.TokenScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: Spring定时任务，获取accessToken存入redis缓存中
 * @author: GaraYing
 * @create: 2018-08-22 11:01
 **/

@Component
public class RedisTimeTask {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisDao redisDao;
    @Value("${APP.APPID}")
    private String appId;
    @Value("${APP.SCOPE}")
    private String scope;
    @Value("${APP.SECRET}")
    private String secret;
    @Value("${YUNZHIJIA.GATEWAY.HOST}")
    private String gatewayHost;


    /**
     * 首次延时1s，然后每90min执行一次
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 90)
    public void task() {
        logger.info(new Date() + "定时任务获取accessToken数据开始存入redis......");
        this.run();
    }

    public void run() {
        String url = gatewayHost.concat("/oauth2/token/getAccessToken");
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> parm = new HashMap<String, String>(5);
        parm.put("scope", scope);
        parm.put("timestamp", timestamp);
        parm.put("appId", appId);
        parm.put("secret", secret);
        JSONObject result = null;
        try {
            result = JSONObject.parseObject(GatewayAuth2.gatewayRequestJson(url, JSONObject.toJSONString(parm))).getJSONObject("data");
            if (result != null && result.containsKey("accessToken")) {
                String accessToken = result.getString("accessToken");
                redisDao.set("accessToken", accessToken,7000L);//设置失效时间
                logger.info("accessToken已存入redis-=-=-=-"+accessToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
