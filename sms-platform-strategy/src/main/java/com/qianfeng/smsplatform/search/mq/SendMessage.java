package com.qianfeng.smsplatform.search.mq;

import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 徐胜涵
 */
@Component
public class SendMessage {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String queue, byte[] bytes) {
        amqpTemplate.convertAndSend(queue, bytes);
    }
}
