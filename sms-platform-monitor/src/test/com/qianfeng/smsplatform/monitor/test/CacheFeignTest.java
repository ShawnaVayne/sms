package com.qianfeng.smsplatform.monitor.test;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.monitor.MonitorApplication;
import com.qianfeng.smsplatform.monitor.feign.CacheFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * @Classname CacheFeignTest
 * @Description TODO
 * @Date 2019/12/6 15:51
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonitorApplication.class)
public class CacheFeignTest {
    @Autowired
    private CacheFeign cacheFeign;

    @Test
    public void testGetKeys(){
        Set<String> keys = cacheFeign.keys(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "*");
        System.err.println(keys);
    }

    @Test
    public void testGetString(){
        String str = cacheFeign.get(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "0");
        System.err.println(str);
    }
}
