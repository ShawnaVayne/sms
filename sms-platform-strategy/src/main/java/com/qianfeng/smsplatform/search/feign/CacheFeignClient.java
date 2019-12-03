package com.qianfeng.smsplatform.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sun.awt.SunHints;

import java.util.List;
import java.util.Set;

@FeignClient("CACHE-SERVICE")
public interface CacheFeignClient {

    @GetMapping("/cache/getLocation")
    String getLocation(@PathVariable("mobileParagraph") String mobileParagraph);

    @GetMapping("/cache/isBlack/{phoneNumber}")
    String isBlack(@PathVariable("phoneNumber") String phoneNumber);

    @GetMapping("/cache/getKeyWords")
    List<String> getKeyWords();

    @GetMapping("/cache/getSize")
    Set getKeySet(@PathVariable("timeQueue") String timeQueue);

    @PostMapping("/cache/setMessage")
    void setMessage(@PathVariable("key") String key, @PathVariable("value") String value);

    @GetMapping("/cache/getFee")
    Long getFee(@PathVariable("client") String client);

    @PostMapping("/cache/decrFee")
    void decrFee(@PathVariable("client") String client, @PathVariable("onceFee") long onceFee);
}
