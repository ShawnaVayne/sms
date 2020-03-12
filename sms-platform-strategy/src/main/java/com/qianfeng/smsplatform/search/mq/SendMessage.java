package com.qianfeng.smsplatform.search.mq;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author simon
 */
@Component
public class SendMessage {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String queue, Standard_Submit submit) {
        amqpTemplate.convertAndSend(queue, submit);
    }

    public void sendMessage(String queue, Standard_Report report) {
        amqpTemplate.convertAndSend(queue, report);
    }
}
