package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 徐胜涵
 */
@Service
public class SmsFeeFilter implements MyFilter {
    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        Long fee = cacheFeignClient.getFee(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + submit.getClientID());
        if (fee < 100) {
            report.setState(2);
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_FEE);
        } else {
            cacheFeignClient.decrFee(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + submit.getClientID(), 100);
        }
    }
}
