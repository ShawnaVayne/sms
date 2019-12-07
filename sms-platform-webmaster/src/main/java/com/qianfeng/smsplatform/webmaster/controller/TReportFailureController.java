package com.qianfeng.smsplatform.webmaster.controller;

import com.qianfeng.smsplatform.webmaster.pojo.TReportFailure;
import com.qianfeng.smsplatform.webmaster.service.TReportFailureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Classname TReportFailureController
 * @Description TODO
 * @Date 2019/12/7 13:53
 * @Created by sunjiangwei
 */
@Controller
public class TReportFailureController {
    @Autowired
    private TReportFailureService tReportFailureService;

    /**
     * 添加推送失败的状态报告
     */
    @ResponseBody
    @RequestMapping("/reportfailure/save/{reportFailure}")
    public int addReportFailure(@PathVariable("reportFailure")  TReportFailure reportFailure){
        int i = tReportFailureService.insert(reportFailure);
        return i;
    }

    @ResponseBody
    @RequestMapping("/reportfailure/find")
    public long findAllReportFailure(){
        List<TReportFailure> tReportFailures = tReportFailureService.selectAll();
        long size = tReportFailures.size();
        return size;
    }
}
