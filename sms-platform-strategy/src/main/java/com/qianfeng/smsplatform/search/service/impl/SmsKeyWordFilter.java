package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 徐胜涵
 */
@Service
public class SmsKeyWordFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        List<String> keyWords = cacheFeignClient.getKeyWords();
        for (String keyWord : keyWords) {
            if (submit.getMessageContent().contains(keyWord)) {
                report.setErrorCode(StrategyConstants.STRATEGY_ERROR_DIRTYWORDS);
                report.setState(2);
            }
        }
    }
}
