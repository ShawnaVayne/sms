package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TClientBusiness;
import com.qianfeng.smsplatform.webmaster.service.ClientBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname ClientBusinessServiceTest
 * @Description TODO
 * @Date 2019/12/4 21:50
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class ClientBusinessServiceTest {
    @Autowired
    private ClientBusinessService clientBusinessService;

    @Test
    public void addClient(){
        TClientBusiness tbc = new TClientBusiness();
        tbc.setPwd("123");
        tbc.setUsercode("admin");
        tbc.setMobile("17638014636");
        tbc.setIpaddress("127.0.0.1");
        clientBusinessService.addClientBusiness(tbc);
    }
}
