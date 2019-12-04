package com.qianfeng.smsplatform.search.feign;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author 徐胜涵
 */
@Component
public class FeignHystrix implements CacheFeignClient {
    @Override
    public String getLocation(String mobileParagraph) {
        return null;
    }

    @Override
    public String isBlack(String phoneNumber) {
        return "error";
    }

    @Override
    public Set<String> getKeyWords(String pattern) {
        return null;
    }

    @Override
    public Set getKeySet(String timeQueue) {
        return null;
    }

    @Override
    public void setMessage(String key, String value) {

    }

    @Override
    public Long getFee(String client) {
        return null;
    }

    @Override
    public void decrFee(String client, long onceFee) {

    }
}
