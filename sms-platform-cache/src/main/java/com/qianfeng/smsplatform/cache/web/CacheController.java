package com.qianfeng.smsplatform.cache.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @RequestMapping("/hmget/{key}")
    public Map<Object,Object> hMGet(@PathVariable("key") String key){
        Map<Object, Object> map = cacheService.hmget(key);
        return map;
    }

    @RequestMapping("/hmset/{key}/{map_json}")
    public boolean hMSet(@PathVariable String key,@PathVariable("map_json") String map_json){
        Map map = JSONObject.parseObject(map_json,Map.class);
        boolean hmset = cacheService.hmset(key, map);
        return hmset;
    }

}
