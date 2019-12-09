package com.qianfeng.smsplatform.search.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @Author 徐胜涵
 */
@Component
@Slf4j
public class FeignHystrix implements CacheFeignClient {
    @Override
    public String getLocation(String mobileParagraph) {
        log.error("地理位置获取降级");
        return null;
    }

    @Override
    public String isBlack(String phoneNumber) {
        log.error("黑名单查询降级");
        return "error";
    }

    @Override
    public Set<String> getKey(String pattern) {
        log.error("获取Key降级");
        return null;
    }

    @Override
    public String getSize(String key) {
        return null;
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        return null;
    }

    @Override
    public Set getKeySet(String timeQueue) {
        return null;
    }

    @Override
    public void setMessage(String key, String value, int expire) {
        log.error("短信设置降级");
    }

    @Override
    public Object getFee(String client) {
        log.error("费用查询降级");
        return 0;
    }

    @Override
    public void decrFee(String client, long onceFee) {
        log.error("扣费降级");
    }
}
