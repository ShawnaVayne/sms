package com.qianfeng.smsplatform.webmaster.service;

import com.qianfeng.smsplatform.webmaster.pojo.TReportFailure;

import java.util.List;

/**
 * @Classname TReportFailureService
 * @Description TODO
 * @Date 2019/12/7 13:49
 * @Created by sunjiangwei
 */

public interface TReportFailureService {
    int insert(TReportFailure reportFailure);

    List<TReportFailure> selectAll();
}
