package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.dao.TReportFailureMapper;
import com.qianfeng.smsplatform.webmaster.pojo.TReportFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Classname TReportFailureMapperTest
 * @Description TODO
 * @Date 2019/12/7 13:34
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class TReportFailureMapperTest {
    @Autowired
    private TReportFailureMapper tReportFailureMapper;

    @Test
    public void testInsert(){
        TReportFailure tpf = new TReportFailure();
        tpf.setClientId(0);
        tpf.setErrorCode("400");
        tpf.setMobile("17638014636");
        int insert = tReportFailureMapper.insert(tpf);
        System.err.println(insert);
    }

    @Test
    public void testSelect(){
        List<TReportFailure> tReportFailures = tReportFailureMapper.selectAll();
        for (TReportFailure tReportFailure : tReportFailures) {
            System.err.println(tReportFailure.toString());
        }
    }
}
