package com.qianfeng.smsplatform.test;

import com.qianfeng.smsplatform.cache.CacheServiceApplication;
import com.qianfeng.smsplatform.cache.service.CacheService;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author simon
 * 2019/12/3 11:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
public class CacheServiceTest {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void TestSet(){
        long money1 = 1000000;
        long money2 = 9829828;
        /*cacheService.set(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "0", money1);
        cacheService.set(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "2", money2);*/
        cacheService.set(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "0", money2);


    }

    @Test
    public void TestGet(){
        Object test4 = cacheService.getObject(CacheConstants.CACHE_PREFIX_CLIENT+"2");
        System.err.println(test4);
    }

    @Test
    public void TestIncr(){
        long result = cacheService.incr(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "0", 1111111);
        System.err.println(result);
    }
    @Test
    public void TestDecr(){
        Integer num = new Integer(11111);
        long test4 = cacheService.decr(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "0", num);
        System.err.println(test4);
    }

    @Test
    public void TestExpire(){
        boolean result = cacheService.expire("test", 3600);
        System.err.println(result);
    }

    @Test
    public void TestGetExpire(){
        long test = cacheService.getExpire("test");
        System.err.println(test);
    }

    @Test
    public void TestHSet(){
        Map<String,Object> map = new HashMap<>(16);
        /*map.put("id",0);
        map.put("corpName","千锋互联");
        map.put("userCode","admin");
        map.put("pwd","71E8700A3759705DF2EA164EB0182E8A");
        map.put("ipAddress","127.0.0.1");
        map.put("isReturnStatus","1");
        map.put("receiveStatusUrl","http://localhost:8099/receive/status");
        map.put("priority",0);
        map.put("userType",2);
        map.put("state",1);
        map.put("mobile","13456785678");*/
        map.put("id",2);
        map.put("clientId",2);
        map.put("extendNumber",9988);
        map.put("price",50);
        map.put("channelId",2);
        boolean result = cacheService.hmset(CacheConstants.CACHE_PREFIX_ROUTER + "2", map);
        System.err.println(result);

    }
    @Test
    public void TestHget(){
        Map<Object, Object> map = cacheService.hmget(CacheConstants.CACHE_PREFIX_CLIENT+"2");
        Set<Map.Entry<Object, Object>> entries = map.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {

            System.out.println(entry.getValue()+" "+entry.getValue().getClass());
        }
        System.err.println(map);
    }
    @Test
    public void TestSize(){
        long map = cacheService.size("test");
        System.err.println(map);
    }
    @Test
    public void testDel(){
        cacheService.del("map:*","tes*");
    }
    @Test
    public void testKeys(){
        Set<String> keys = cacheService.keys("man:*");
        for (String key : keys) {
            System.err.println(key);
        }
    }
}
