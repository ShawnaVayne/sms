package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author simon
 */
@Service("smsOpIDFilter")
public class SmsOpIDFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) {
        String mobileParagraph = submit.getDestMobile().substring(0, 7);
        String location = cacheFeignClient.getLocation(CacheConstants.CACHE_PREFIX_PHASE + mobileParagraph);
        //如果是空说明服务器挂了或者没有查到
        if (location != null && !location.equals("0")) {
            int province = new Integer(location.split("&")[0]);
            int city = new Integer(location.split("&")[1]);
            submit.setProvinceId(province);
            submit.setCityId(city);
        }

    }
}
