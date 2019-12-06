package com.qianfeng.smsplatform.monitor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

/**
 * @Classname CacheFeign
 * @Description TODO
 * @Date 2019/12/6 12:48
 * @Created by sunjiangwei
 */
@FeignClient(value = "CACHE-SERVICE")
public interface CacheFeign {

    /**
     * 往redis中设置string类型，值为object类型的数据
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/setObj/{key}/{value}")
    Boolean setObject(@PathVariable String key,@PathVariable Object value);

    /**
     * todo:从redis中获取String类型的key对应的数据
     * @param key
     * @return
     */
    @RequestMapping("/cache/getObject/{key}")
    Object getObject(@PathVariable String key);

    @RequestMapping("/cache/get/{key}")
    String get(@PathVariable String key);

    /**
     * 根据key删除redis中存储的数据
     * @param keys
     */
    @RequestMapping("/cache/del/{keys}")
    void del(@PathVariable String... keys);

    /**
     * 模糊查找，查找所有已固定字符开头的数据
     * @param pattern
     * @return
     */
    @RequestMapping("/cache/keys/{pattern}")
    Set<String> keys(@PathVariable String pattern);

    /**
     * 向redis中插入String类型的数据，如果存在则插入失败，如果不存在则插入成功！
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/cache/setnx/{key}/{value}")
    boolean setNx(@PathVariable String key,@PathVariable String value);

}
