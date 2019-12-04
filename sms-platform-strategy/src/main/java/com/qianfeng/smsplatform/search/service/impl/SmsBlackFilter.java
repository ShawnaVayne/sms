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
public class SmsBlackFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        String isblack = cacheFeignClient.isBlack(CacheConstants.CACHE_PREFIX_BLACK + submit.getDestMobile());
        System.out.println(isblack);
        //如果是error说明服务器挂了
        if (isblack.equals("error") || isblack.equals("1")) {
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_BLACK);
            report.setState(2);
        }
    }
}
