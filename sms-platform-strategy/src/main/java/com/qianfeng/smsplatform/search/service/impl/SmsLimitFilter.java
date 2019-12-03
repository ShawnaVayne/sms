package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

/**
 * @Author 徐胜涵
 */
@Service
public class SmsLimitFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        Set minutesSet = cacheFeignClient.getKeySet(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getSrcNumber() + submit.getMessageContent() + "*");
        Set hourSet = cacheFeignClient.getKeySet(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getSrcNumber() + submit.getMessageContent() + "*");
        Set daySet = cacheFeignClient.getKeySet(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getSrcNumber() + submit.getMessageContent() + "*");
        int minutesSize = minutesSet.size();
        int hourSize = hourSet.size();
        int daySize = daySet.size();
        Long currentTime = System.currentTimeMillis();
        if (minutesSize < 3 && hourSize < 5 && daySize < 10) {
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getSrcNumber() + submit.getMessageContent() + currentTime, "1");
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getSrcNumber() + submit.getMessageContent() + currentTime, "1");
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getSrcNumber() + submit.getMessageContent() + currentTime, "1");
        } else {
            report.setState(2);
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_LIMIT);
        }
    }
}
