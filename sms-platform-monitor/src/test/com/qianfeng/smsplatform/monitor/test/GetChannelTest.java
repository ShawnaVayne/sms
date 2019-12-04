package com.qianfeng.smsplatform.monitor.test;

import com.qianfeng.smsplatform.monitor.MonitorApplication;
import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import com.qianfeng.smsplatform.monitor.pojo.TChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import java.util.List;

/**
 * @Classname GetChannelTest
 * @Description TODO
 * @Date 2019/12/3 17:30
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonitorApplication.class)
public class GetChannelTest {
    @Autowired
    private ChannelFeign channelFeign;

    @Test
    public void testGetAll(){
        List<Long> nums = channelFeign.getAllChannel();
        System.err.println(nums);
    }
}
