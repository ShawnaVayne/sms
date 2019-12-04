package com.qianfeng.smsplatform.webmaster.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname CacheFeignTest
 * @Description TODO
 * @Date 2019/12/4 17:14
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class CacheFeignTest {
    @Autowired
    private CacheFeign cacheFeign;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testhMget(){
        Map<Object, Object> objectMap = cacheFeign.hMGet(CacheConstants.CACHE_PREFIX_CLIENT + "0");
        System.err.println(objectMap);
    }

    @Test
    public void testHMset() throws IOException {

        Map<String ,Object> map = new HashMap<>();
        map.put("name","sjw");
        map.put("age",123);
        map.put("id",12);
        String mapJson = objectMapper.writeValueAsString(map);
        boolean b = cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_CLIENT+map.get("id"), map);
        System.err.println(b);
    }
}
