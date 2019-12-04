package com.qianfeng.smsplatform.search.mq;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.config.QueueConfig;
import com.qianfeng.smsplatform.search.service.MyFilter;
import com.qianfeng.smsplatform.search.service.impl.SmsBlackFilter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class ReciveMessage {

    @Autowired
    private Map<String, MyFilter> filterList;
    @Value("${smsplatform.filters}")
    private String filterNames;
    @Autowired
    private SendMessage sendMessage;

    /**
     * todo: 每次接受信息进行处理
     * @param submit
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws TimeoutException
     */
    @RabbitListener(queues = RabbitMqConsants.TOPIC_PRE_SEND, autoStartup = "true", containerFactory = "customContainerFactory")
    public void getMessage(Standard_Submit submit) throws IOException, ClassNotFoundException, TimeoutException {
        String[] filterNameArray = filterNames.split(",");
        //从mq中反序列化发送来的对象
        Standard_Report report = new Standard_Report();
        //客户ID
        report.setClientID(submit.getClientID());
        //目的手机号
        report.setMobile(submit.getDestMobile());
        //客户侧唯一序列号
        report.setSrcID(submit.getSrcSequenceId());
        //待发送状态
        report.setState(1);
        /*System.out.println(submit);
        System.out.println(filterNames);
        System.out.println("filterSize" + filterList.size());*/
        for (int i = 0; i < filterNameArray.length; i++) {
            //System.out.println(filterNameArray[i]);
            MyFilter myFilter = filterList.get(filterNameArray[i]);

            myFilter.doFilter(submit, report);
            //如果是状态2(发送失败)，就跳出循环
            System.err.println("state" + report.getState());
            if (report.getState() == 2) {
                break;
            }
        }
        System.err.println(submit);
        System.err.println(report);
        //把report的状态同步到submit
        submit.setReportState(report.getState());
        //状态报告发送次数
        report.setSendCount(1);

        //如果状态不是发送失败状态
        if (report.getState() != 2) {
            System.err.println("发送待发日志");
            //序列化submit，将submit发送到网关队列
            //发送待发日志
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID(), submit);

        } else {
            System.err.println("发送下发日志");
            //发送下发日志
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_SEND_LOG, submit);
        }
        //发送状态报告
        System.err.println("发送状态报告");
        sendMessage.sendMessage(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, report);
    }

//    /**
//     * 目的号码
//     */
//    private String mobile;
//
//    /**
//     * 定长4个字节 发送状态 0 发送成功 1 等待发送 2 发送失败 (只适用于移动联通)
//     */
//    private int state;
//
//    /**
//     * 具体发送状态 (详细见移动联通电信协议)
//     */
//    private String errorCode;
//
//    /**
//     * 客户侧唯一序列号
//     */
//    private long srcID;
//    /**
//     * 客户ID
//     */
//    private long clientID;
//    /**
//     * 响应返回的运营商消息编号,
//     */
//    private String msgId;
//    /**状态报告推送次数*/
//    private int sendCount;
}
