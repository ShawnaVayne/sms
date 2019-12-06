package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import com.qianfeng.smsplatform.search.util.CheckPhone;
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
        //如果是手机号或者座机号
        if (CheckPhone.isPhoneOrTel(submit.getSrcNumber())) {
            //移动
            if (CheckPhone.isChinaMobilePhoneNum(submit.getSrcNumber())) {
                submit.setOperatorId(1);
            }
            //联通
            if (CheckPhone.isChinaUnicomPhoneNum(submit.getSrcNumber())) {
                submit.setOperatorId(2);
            }
            //电信
            if (CheckPhone.isChinaTelecomPhoneNum(submit.getSrcNumber())) {
                submit.setOperatorId(3);
            }
            Map<Object, Object> hmget = cacheFeignClient.hmget(CacheConstants.CACHE_PREFIX_ROUTER + submit.getClientID());
            //通道Id
            Object channelId = hmget.get("channelId");
            if (channelId == null) {
                report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
                report.setState(2);
            } else {
                Object extendNumber = hmget.get("extendNumber");
                //设置网关id
                submit.setGatewayID((Integer) channelId);
                //将扩展号码添加到源手机号的后面
                submit.setSrcNumber(submit.getSrcNumber() + extendNumber);
            }
        }
        //如果验证不是手机号或者座机号直接路由错误
        else {
            report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
            report.setState(2);
        }
    }
}
