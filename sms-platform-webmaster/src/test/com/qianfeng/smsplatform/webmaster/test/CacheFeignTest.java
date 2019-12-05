package com.qianfeng.smsplatform.webmaster.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TClientBusiness;
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

        TClientBusiness tcb = new TClientBusiness();
        tcb.setId(12L);
        tcb.setPwd("123456");
        tcb.setMobile("11011111111");
        String json = objectMapper.writeValueAsString(tcb);
        boolean b = cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_CLIENT+tcb.getId(), json);
        System.err.println(b);
    }
}
