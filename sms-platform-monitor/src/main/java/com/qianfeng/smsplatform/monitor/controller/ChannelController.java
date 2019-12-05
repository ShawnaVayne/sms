package com.qianfeng.smsplatform.monitor.controller;

import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname ChannelListController
 * @Description TODO
 * @Date 2019/12/3 15:17
 * @Created by sunjiangwei
 */
@RestController
public class ChannelController {
    @Autowired
    private ChannelFeign channelFeign;

    @RequestMapping("/getAllChannel")
    public List<Long> getAllChannel(){
        return channelFeign.getAllChannel();
    }

}
