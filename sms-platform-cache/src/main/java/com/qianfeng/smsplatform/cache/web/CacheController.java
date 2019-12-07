package com.qianfeng.smsplatform.cache.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @author simon
 * 2019/12/3 16:36
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ObjectMapper objectMapper;
    @RequestMapping("/setObj/{key}/{value}/{expireTime}")
    public Boolean setObjectWithExpire(@PathVariable String key,@PathVariable Object value,@PathVariable int expireTime){
        Boolean result = cacheService.set(key, value, expireTime);
        return result;
    }
    @RequestMapping("/set/{key}/{value}/{expireTime}")
    public Boolean setWithExpire(@PathVariable String key,@PathVariable String value,@PathVariable int expireTime){
        Boolean result = cacheService.set(key, value, expireTime);
        return result;
    }
    @RequestMapping("/setLong/{key}/{value}")
    public Boolean setLong(@PathVariable String key,@PathVariable long value){
        Boolean result = cacheService.set(key, value);
        return result;
    }
    @RequestMapping("/setObj/{key}/{value}")
    public Boolean setObject(@PathVariable String key,@PathVariable Object value){
        Boolean result = cacheService.set(key, value);
        return result;
    }
    @RequestMapping("/set/{key}/{value}")
    public Boolean set(@PathVariable String key,@PathVariable String value){
        Boolean result = cacheService.set(key, value);
        return result;
    }
    @RequestMapping("/get/{key}")
    public String get(@PathVariable String key){
        String value = cacheService.get(key);
        return value;
    }
    @RequestMapping("/getSet/{key}/{value}")
    public Object getAndSet(@PathVariable String key,@PathVariable String value){
        Object result = cacheService.getAndSet(key, value);
        return result;
    }
    @RequestMapping("/getObject/{key}")
    public Object getObject(@PathVariable String key){
        Object object = cacheService.getObject(key);
        return object;
    }
    @RequestMapping("/size/{key}")
    public long size(@PathVariable String key){
        long size = cacheService.size(key);
        return size;
    }
    @RequestMapping("del/{keys}")
    public void del(@PathVariable String... keys){
        cacheService.del(keys);
    }
    @RequestMapping("/expire/{key}/{seconds}")
    public boolean expire(@PathVariable String key,@PathVariable long seconds){
        boolean result = cacheService.expire(key, seconds);
        return result;
    }
    @RequestMapping("/getExpire/{key}")
    public long getExpire(@PathVariable String key){
        long expire = cacheService.getExpire(key);
        return expire;
    }
    @RequestMapping("/incr/{key}/{delta}")
    public long incr(@PathVariable String key,@PathVariable long delta){
        long incr = cacheService.incr(key, delta);
        return incr;
    }
    @RequestMapping("/decr/{key}/{delta}")
    public long decr(@PathVariable String key,@PathVariable long delta){
        long decr = cacheService.decr(key, delta);
        return decr;
    }
    @RequestMapping("/keys/{pattern}")
    public Set<String> keys(@PathVariable String pattern){
        Set<String> keys = cacheService.keys(pattern);
        return keys;
    }
    @RequestMapping("/hmget/{key}")
    public Map<Object,Object> hMGet(@PathVariable String key){
        Map<Object, Object> map = cacheService.hmget(key);
        return map;
    }

    @RequestMapping("/hmset/{key}/{map_json}")
    public boolean hMSet(@PathVariable String key,@PathVariable String map_json){
        Map map = JSONObject.parseObject(map_json,Map.class);
        boolean hmset = cacheService.hmset(key, map);
        return hmset;
    }
    @RequestMapping("/setnx/{key}/{value}")
    public boolean setNx(@PathVariable String key,@PathVariable String value){
        boolean result = cacheService.setnx(key, value);
        return result;
    }
}
