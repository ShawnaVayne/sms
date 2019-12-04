package com.qianfeng.smsplatform.userinterface.config;


import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



/**
 * 2019/12/316:07
 * <p>
 * 未知的事情 永远充满变数
 */

@SpringBootApplication
public class SendMq {

    @Bean
    public Queue queue123(){
        return new Queue(RabbitMqConsants.TOPIC_PRE_SEND);
    }
}
