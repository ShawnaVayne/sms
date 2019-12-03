package com.qianfeng.smsplatform.cache.service.impl;

import com.qianfeng.smsplatform.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author simon
 * 2019/12/3 9:32
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Boolean set(String key, Object value, int expireTime) {
        try {
            template.opsForValue().set(key,value.toString());
            if(expireTime>0){
                template.expire(key,expireTime,TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean set(String key, String value, int expireTime) {
        try {
            template.opsForValue().set(key,value);
            if(expireTime>0){
                template.expire(key,expireTime,TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean set(String key, Object value) {
        try{
            template.opsForValue().set(key,value.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean set(String key, String value) {
        try{
            template.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String get(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public Object getAndSet(String key, String value) {
        return template.opsForValue().getAndSet(key, value);
    }

    @Override
    public Object getObject(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public long size(String key) {
        Long size = template.opsForValue().size(key);
        return size;
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            template.delete(key);
        }
    }

    @Override
    public boolean expire(String key, long seconds) {
        Boolean expire = template.expire(key, seconds, TimeUnit.SECONDS);
        return expire;
    }

    @Override
    public long getExpire(String key) {
        Long expire = template.getExpire(key);
        return expire;
    }

    @Override
    public long incr(String key, long delta) {
        Long increment = template.opsForValue().increment(key, delta);
        return increment;
    }

    @Override
    public long decr(String key, long delta) {
        Long increment = template.opsForValue().increment(key, 0-delta);
        return increment;
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> keys = template.keys(pattern);
        return keys;
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        Map<Object, Object> entries = template.opsForHash().entries(key);
        if(entries!=null){
            return entries;
        }
        return null;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        try{
            template.opsForHash().putAll(key,map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
