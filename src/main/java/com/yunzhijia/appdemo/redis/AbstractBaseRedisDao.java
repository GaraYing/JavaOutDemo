package com.yunzhijia.appdemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @description: 暂时不用
 * @author: GaraYing
 * @create: 2018-08-20 14:00
 **/

public abstract class AbstractBaseRedisDao<K, V> {
    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    protected RedisSerializer<String> getRedisSerializer(){
        return redisTemplate.getStringSerializer();
    }
}
