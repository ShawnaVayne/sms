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
 * @Author 徐胜涵
 */
@Service
@Slf4j
public class SmsFeeFilter implements MyFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        Integer fee = (Integer) cacheFeignClient.getFee(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + submit.getClientID());
        Map<Object, Object> hmget = cacheFeignClient.hmget(CacheConstants.CACHE_PREFIX_ROUTER + submit.getClientID());
        Integer price = (Integer) hmget.get("price");
        //如果剩余的钱少于一次发送的费用就报错
        if (fee < price) {
            log.error("费用不足");
            report.setState(2);
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_FEE);
        } else {
            log.info("扣费{}厘", price);
            cacheFeignClient.decrFee(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + submit.getClientID(), price);
        }
    }
}
