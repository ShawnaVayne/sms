package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname BlackCacheFeignTest
 * @Description TODO
 * @Date 2019/12/4 15:41
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class BlackCacheFeignTest {
    @Autowired
    private CacheFeign cacheFeign;

    @Test
    public void testAddData(){
        Boolean b = cacheFeign.set(CacheConstants.CACHE_PREFIX_BLACK + "17638014632", "1");
        System.err.println(b);
    }

    @Test
    public void testUpdateData(){

    }
}
