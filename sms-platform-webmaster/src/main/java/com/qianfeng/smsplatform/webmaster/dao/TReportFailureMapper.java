package com.qianfeng.smsplatform.webmaster.dao;

import com.qianfeng.smsplatform.common.model.Standard_Report;

import java.util.List;

/**
 * @Classname TReportFailureMapper
 * @Description TODO
 * @Date 2019/12/7 12:52
 * @Created by sunjiangwei
 */
public interface TReportFailureMapper {
    int insert(Standard_Report reportFailure);

    List<Standard_Report> selectAll();
}
