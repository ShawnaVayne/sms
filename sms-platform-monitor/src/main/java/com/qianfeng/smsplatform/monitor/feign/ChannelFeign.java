package com.qianfeng.smsplatform.monitor.feign;

import com.qianfeng.smsplatform.monitor.pojo.TChannel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.rmi.runtime.Log;

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
}
