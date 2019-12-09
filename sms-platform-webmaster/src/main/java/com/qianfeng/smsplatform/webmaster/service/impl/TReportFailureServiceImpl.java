package com.qianfeng.smsplatform.webmaster.service.impl;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.webmaster.dao.TReportFailureMapper;
import com.qianfeng.smsplatform.webmaster.service.TReportFailureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname TReportFailureServiceImpl
 * @Description TODO
 * @Date 2019/12/7 13:51
 * @Created by sunjiangwei
 */
@Service
public class TReportFailureServiceImpl implements TReportFailureService {
    @Autowired
    private TReportFailureMapper tReportFailureMapper;

    @Override
    public int insert(Standard_Report reportFailure) {
        return tReportFailureMapper.insert(reportFailure);
    }

    @Override
    public List<Standard_Report> selectAll() {
        return tReportFailureMapper.selectAll();
    }
}
