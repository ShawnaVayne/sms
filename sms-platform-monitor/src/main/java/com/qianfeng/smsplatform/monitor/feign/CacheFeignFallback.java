package com.qianfeng.smsplatform.monitor.feign;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Classname CacheFeignFallback
 * @Description TODO
 * @Date 2019/12/6 17:17
 * @Created by sunjiangwei
 */
@Component
public class CacheFeignFallback implements CacheFeign {
    @Override
    public Boolean setObject(String key, Object value) {
        return null;
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void del(String... keys) {

    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public boolean setNx(String key, String value) {
        return false;
    }
}
