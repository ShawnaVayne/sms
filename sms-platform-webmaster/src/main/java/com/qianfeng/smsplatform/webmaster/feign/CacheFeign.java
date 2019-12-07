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
     * 设置String类型的数据，value为String类型
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/set/{key}/{value}")
    Boolean set(@PathVariable("key") String key, @PathVariable("value") String value);

    /**
     * 设置String类型数据，value为Object类型
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/setObj/{key}/{value}")
    Boolean setObject(@PathVariable String key,@PathVariable Object value);


    /**
     * 设置String类型数据，value为Long类型
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/setLong/{key}/{value}")
    Boolean setLong(@PathVariable String key,@PathVariable long value);
    /**
     * 获取redis中String类型的数据
     * @param key
     * @return
     */
    @RequestMapping("/cache/get/{key}")
    String get(@PathVariable String key);

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
     * @param map_json
     * @return
     */
<<<<<<< HEAD
    @RequestMapping("/cache/hmset/{key}/{map}")
    boolean hMSet(@PathVariable("key") String key,@PathVariable("map") Map<String, Object> map);
=======
    @RequestMapping("/cache/hmset/{key}/{map_json}")
    boolean hMSet(@PathVariable String key,@PathVariable String map_json);
>>>>>>> 81af1556d43dae2aac835412aaf44267e6416850
}
