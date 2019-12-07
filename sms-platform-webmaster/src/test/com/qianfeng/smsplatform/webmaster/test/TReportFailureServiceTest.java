package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.service.TReportFailureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname TReportFailureServiceTest
 * @Description TODO
 * @Date 2019/12/7 16:27
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class TReportFailureServiceTest {
    @Autowired
    private TReportFailureService tReportFailureService;

    @Test
    public void testInsert(){
        Standard_Report tpf = new Standard_Report();
        tpf.setClientID(2);
        tpf.setErrorCode("400");
        tpf.setMobile("17638014636");
        tpf.setSrcID(5);
        int insert = tReportFailureService.insert(tpf);
        System.err.println(insert);
    }
}
