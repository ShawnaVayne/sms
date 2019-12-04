package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author 徐胜涵
 */
@Service
public class SmsRouterFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        Map<Object, Object> hmget = cacheFeignClient.hmget(CacheConstants.CACHE_PREFIX_ROUTER + submit.getClientID());
        //通道Id
        Object channelId = hmget.get("channelId");
        if (channelId == null) {
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
            report.setState(2);
        } else {
            Object extendNumber = hmget.get("extendNumber");
            submit.setOperatorId((Integer) channelId);
            submit.setSrcNumber(submit.getSrcNumber() + extendNumber);
        }
    }
}
