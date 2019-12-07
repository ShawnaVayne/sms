package com.qianfeng.smsplatform.webmaster.dao;

import com.qianfeng.smsplatform.webmaster.pojo.TReportFailure;

import java.util.List;

/**
 * @Classname TReportFailureMapper
 * @Description TODO
 * @Date 2019/12/7 12:52
 * @Created by sunjiangwei
 */
public interface TReportFailureMapper {
    int insert(TReportFailure reportFailure);

    List<TReportFailure> selectAll();
}
