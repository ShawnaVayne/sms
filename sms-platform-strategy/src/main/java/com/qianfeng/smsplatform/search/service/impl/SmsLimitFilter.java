package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @Author 徐胜涵
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
        //将用户手机号设置到submit中
        Object mobile = clientMap.get("mobile");
        log.info("用户手机号：{}", mobile);
        if (mobile == null) {
            report.setState(2);
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_LIMIT);
        } else {
            submit.setSrcNumber((String) mobile);
            Set<String> minutesSet = cacheFeignClient.getKey(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + "*");
            Set<String> hourSet = cacheFeignClient.getKey(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + "*");
            Set<String> daySet = cacheFeignClient.getKey(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + "*");
            int minutesSize = minutesSet.size();
            int hourSize = hourSet.size();
            int daySize = daySet.size();
            if (minutesSize < 3 && hourSize < 5 && daySize < 10) {
                /*log.info("向redis中储存短信");
                cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + currentTime, "1");
                cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + currentTime, "1");
                cacheFeignClient.setMessage(CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + submit.getSrcNumber() + submit.getDestMobile() + submit.getMessageContent() + currentTime, "1");*/
            } else {
                report.setState(2);
                report.setErrorCode(StrategyConstants.STRATEGY_ERROR_LIMIT);
            }
        }
    }
}
