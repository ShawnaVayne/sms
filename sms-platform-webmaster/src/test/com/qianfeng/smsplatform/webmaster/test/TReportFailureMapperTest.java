package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.dao.TReportFailureMapper;
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
        Standard_Report tpf = new Standard_Report();
        tpf.setClientID(2);
        tpf.setErrorCode("400");
        tpf.setMobile("17638014636");
        tpf.setSrcID(5);
        int insert = tReportFailureMapper.insert(tpf);
        System.err.println(insert);
    }

    @Test
    public void testSelect(){
        List<Standard_Report> tReportFailures = tReportFailureMapper.selectAll();
        for (Standard_Report tReportFailure : tReportFailures) {
            System.err.println(tReportFailure.toString());
        }
    }
}
