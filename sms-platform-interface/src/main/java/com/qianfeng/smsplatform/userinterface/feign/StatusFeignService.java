package com.qianfeng.smsplatform.userinterface.feign;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2019/12/711:39
 * <p>
 * 未知的事情 永远充满变数
 */

@FeignClient("SMS-PLATFORM-WEBMASTER")
public interface StatusFeignService {


    @RequestMapping("/reportfailure/save/{reportFailure}")
    public int addReportFailure(@PathVariable("reportFailure") Standard_Report standard_report);


}