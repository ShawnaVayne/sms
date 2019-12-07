package com.qianfeng.smsplatform.monitor.feign;

import com.qianfeng.smsplatform.monitor.pojo.TClientBusiness;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Classname ChannelFeign
 * @Description TODO
 * @Date 2019/12/3 15:01
 * @Created by sunjiangwei
 */
@FeignClient(value = "SMS-PLATFORM-WEBMASTER")
public interface ChannelFeign {

    @RequestMapping("/getAllChannel")
    List<Long> getAllChannel();

    //根据用户id查询用户
    @RequestMapping("/sys/getClientBusiness/{id}")
    TClientBusiness findClientBusinessById(@PathVariable Long id);

    //获取状态报告表中是否有数据
    @RequestMapping("/reportfailure/find")
    long findAllReportFailure();
}
