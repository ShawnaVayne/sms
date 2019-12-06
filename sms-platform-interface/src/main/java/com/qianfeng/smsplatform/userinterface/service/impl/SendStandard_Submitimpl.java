package com.qianfeng.smsplatform.userinterface.service.impl;

import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.userinterface.service.SendStandard_Submit;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2019/12/315:50
 * <p>
 * 未知的事情 永远充满变数
 */
@Service
public class SendStandard_Submitimpl implements SendStandard_Submit {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void setMessage(String queue, Standard_Submit standard_submit){
        amqpTemplate.convertAndSend(queue,standard_submit);
    }
}
