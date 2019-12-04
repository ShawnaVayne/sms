package com.qianfeng.smsplatform.userinterface.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 2019/12/319:42
 * <p>
 * 未知的事情 永远充满变数
 */        // CACHE-SERVICE
@FeignClient("CACHE-SERVICE")
public interface CacheFeignService {


    @RequestMapping("/cache/hmget/{key}")
    public Map<Object,Object> hMGet(@PathVariable("key") String key);
    @RequestMapping("/cache/hmset/{key}/{map}")
    public boolean hMSet(@PathVariable("key") String key,@PathVariable("map") Map<String, Object> map);


    }
