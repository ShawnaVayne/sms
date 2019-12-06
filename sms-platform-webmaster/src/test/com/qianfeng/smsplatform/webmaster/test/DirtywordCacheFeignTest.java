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
 * @Classname DirtywordCacheFeignTest
 * @Description TODO
 * @Date 2019/12/4 16:09
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class DirtywordCacheFeignTest {
    @Autowired
    private CacheFeign cacheFeign;

    @Test
    public void addDrity(){
        Boolean b = cacheFeign.set(CacheConstants.CACHE_PREFIX_DIRTYWORDS + "dance", "1");
        System.err.println(b);
    }

    @Test
    public void updateDirty(){
        cacheFeign.del(CacheConstants.CACHE_PREFIX_DIRTYWORDS + "dance");
    }
}
