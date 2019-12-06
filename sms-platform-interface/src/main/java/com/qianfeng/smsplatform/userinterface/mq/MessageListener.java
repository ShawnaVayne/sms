package com.qianfeng.smsplatform.userinterface.mq;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.userinterface.feign.CacheFeignService;
import com.qianfeng.smsplatform.userinterface.util.Httpsend;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 2019/12/418:01
 * <p>
 * 未知的事情 永远充满变数
 */
@Component
//@RabbitListener(queues = RabbitMqConsants.TOPIC_PUSH_SMS_REPORT)
public class MessageListener {

    @Autowired
    private CacheFeignService cacheFeignService;

    @RabbitListener(queues = RabbitMqConsants.TOPIC_PUSH_SMS_REPORT,autoStartup = "true")//指定当前方法是用于处理哪个队列的消息的,不需要在类上面添加注解
    public void lw(Standard_Report message) {
        System.err.println("收到了消息====>" + message);

        long clientID = message.getClientID();
        Map<Object, Object> objectObjectMap = cacheFeignService.hMGet(CacheConstants.CACHE_PREFIX_CLIENT + clientID);
        String url = (String)objectObjectMap.get("receiveStatusUrl");

        System.out.println("用户的url:"+url);
        Httpsend.Send(url,message );
        System.out.println("发送完成！");
    }


}
