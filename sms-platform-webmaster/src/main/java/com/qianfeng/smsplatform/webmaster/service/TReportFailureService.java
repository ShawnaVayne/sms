package com.qianfeng.smsplatform.webmaster.service;

import com.qianfeng.smsplatform.common.model.Standard_Report;

import java.util.List;

/**
 * @Classname TReportFailureService
 * @Description TODO
 * @Date 2019/12/7 13:49
 * @Created by sunjiangwei
 */

public interface TReportFailureService {
    int insert(Standard_Report reportFailure);

    List<Standard_Report> selectAll();
}
