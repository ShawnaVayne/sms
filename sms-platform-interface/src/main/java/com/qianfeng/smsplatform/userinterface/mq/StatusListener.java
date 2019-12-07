package com.qianfeng.smsplatform.userinterface.mq;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.userinterface.feign.StatusFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2019/12/711:32
 * <p>
 * 未知的事情 永远充满变数
 */
@Slf4j
public class StatusListener {

    @Autowired
    private StatusFeignService statusFeignService;



    @RabbitListener(queues = RabbitMqConsants.TOPIC_SMS_REPORT_FAILURE,autoStartup = "true")//指定当前方法是用于处理哪个队列的消息的,不需要在类上面添加注解
    public void reciveMessage(Standard_Report message) {
        log.info("收到信息："+message);





    }

}
