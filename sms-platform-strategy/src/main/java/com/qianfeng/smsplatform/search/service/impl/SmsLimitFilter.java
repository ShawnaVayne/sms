package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author simon
 */
@Service
@Slf4j
public class SmsLimitFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;


    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        //从缓存把用户信息拿出来
        Map<Object, Object> clientMap = cacheFeignClient.hmget(CacheConstants.CACHE_PREFIX_CLIENT + submit.getClientID());
        String minute = cacheFeignClient.getSize(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent());
        String hour = cacheFeignClient.getSize(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent());
        String day = cacheFeignClient.getSize(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent());
        Integer minutesSize = 0;
        Integer hourSize = 0;
        Integer daySize = 0;
        if (minute != null) {
            minutesSize = new Integer(minute);
        }
        if (hour != null) {
            hourSize = new Integer(hour);
        }
        if (day != null) {
            daySize = new Integer(day);
        }
        if (minutesSize < 3 && hourSize < 5 && daySize < 10) {
            log.info("向redis中储存短信");
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent(), minutesSize + 1 + "", 300);
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent(), hourSize + 1 + "", 3600);
            cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getClientID() + submit.getDestMobile() + submit.getMessageContent(), daySize + 1 + "", 86400);
        } else {
            report.setState(2);
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_LIMIT);
        }
    }
}
