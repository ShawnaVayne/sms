package com.qianfeng.smsplatform.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "CACHE-SERVICE", fallback = FeignHystrix.class)
public interface CacheFeignClient {

    @GetMapping("/cache/get/{key}")
    String getLocation(@PathVariable("key") String mobileParagraph);

    @RequestMapping("/cache/get/{key}")
    String isBlack(@PathVariable("key") String phoneNumber);

    @GetMapping("/cache/keys/{pattern}")
    Set<String> getKey(@PathVariable("pattern") String pattern);
    @RequestMapping("/cache/hmget/{key}")
    Map<Object, Object> hmget(@PathVariable("key") String key);

    @GetMapping("/cache/getSize")
    Set getKeySet(@PathVariable("timeQueue") String timeQueue);

    @RequestMapping("/cache/set/{key}/{value}")
    void setMessage(@PathVariable("key") String key, @PathVariable("value") String value);

    @RequestMapping("/cache/getObject/{key}")
    Object getFee(@PathVariable("key") String client);

    @RequestMapping("/cache/decr/{key}/{delta}")
    void decrFee(@PathVariable("key") String key, @PathVariable("delta") long delta);
}
