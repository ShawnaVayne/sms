package com.qianfeng.smsplatform.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@FeignClient(value = "CACHE-SERVICE", fallback = FeignHystrix.class)
public interface CacheFeignClient {

    @GetMapping("/cache/get/{key}")
    String getLocation(@PathVariable("key") String mobileParagraph);

    @RequestMapping("/cache/get/{key}")
    String isBlack(@PathVariable("key") String phoneNumber);

    @GetMapping("/cache/keys/{pattern}")
    Set<String> getKeyWords(@PathVariable("pattern") String pattern);

    @GetMapping("/cache/getSize")
    Set getKeySet(@PathVariable("timeQueue") String timeQueue);

    @PostMapping("/cache/setMessage")
    void setMessage(@PathVariable("key") String key, @PathVariable("value") String value);

    @GetMapping("/cache/getFee")
    Long getFee(@PathVariable("client") String client);

    @PostMapping("/cache/decrFee")
    void decrFee(@PathVariable("client") String client, @PathVariable("onceFee") long onceFee);
}
