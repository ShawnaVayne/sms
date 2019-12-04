package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname ReciveMessageFromQueue
 * @Description TODO
 * @Date 2019/12/3 13:38
 * @Created by sunjiangwei
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class ReciveMessageFromQueue {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void testRecive(){
        Message message = rabbitTemplate.receive(RabbitMqConsants.TOPIC_PRE_SEND);
        String jsonObj = new String(message.getBody());
        System.err.println("收到的消息" + jsonObj);
    }
}
