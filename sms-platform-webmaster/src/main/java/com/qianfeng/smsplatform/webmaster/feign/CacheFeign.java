package com.qianfeng.smsplatform.webmaster.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Classname CacheFeign
 * @Description TODO
 * @Date 2019/12/4 12:52
 * @Created by sunjiangwei
 */
@FeignClient("CACHE-SERVICE")
public interface CacheFeign {
    /**
     * 设置String类型的数据
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/set/{key}/{value}")
    Boolean set(@PathVariable("key") String key, @PathVariable("value") String value);

    /**
     * 根据key值删除缓存中的数据
     * @param keys
     */
    @RequestMapping("/cache/del/{keys}")
    void del(@PathVariable("keys") String... keys);

    /**
     * 根据key值获取redis中map
     * @param key
     * @return
     */
    @RequestMapping("/cache/hmget/{key}")
    Map<Object,Object> hMGet(@PathVariable("key") String key);


    /**
     * 向redis中插入hash类型的数据
     * @param key
     * @param map
     * @return
     */
    @RequestMapping("/cache/hmset/{key}/{map}")
    boolean hMSet(@PathVariable("key") String key,@PathVariable("map") Map<String, Object> map);
}
