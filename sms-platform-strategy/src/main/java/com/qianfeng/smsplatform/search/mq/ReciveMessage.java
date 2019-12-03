package com.qianfeng.smsplatform.search.mq;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.config.QueueConfig;
import com.qianfeng.smsplatform.search.service.MyFilter;
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
    @Autowired
    private QueueConfig queueConfig;

    /**
     * todo: 每次接受信息进行处理
     * @param bytes
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws TimeoutException
     */
    @RabbitListener(queues = RabbitMqConsants.TOPIC_PRE_SEND, autoStartup = "true", containerFactory = "customContainerFactory")
    public void getMessage(byte[] bytes) throws IOException, ClassNotFoundException, TimeoutException {
        String[] filterNameArray = filterNames.split(",");
        //从mq中反序列化发送来的对象
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        GenericMessage genericMessage = (GenericMessage) ois.readObject();
        Standard_Submit submit = (Standard_Submit) genericMessage.getPayload();
        Standard_Report report = new Standard_Report();
        //客户ID
        report.setClientID(submit.getClientID());
        //目的手机号
        report.setMobile(submit.getDestMobile());
        //客户侧唯一序列号
        report.setSrcID(submit.getSrcSequenceId());
        //待发送状态
        report.setState(1);
        System.out.println(submit);
        for (int i = 0; i < filterNameArray.length; i++) {
            MyFilter myFilter = filterList.get(filterNameArray[i]);
            myFilter.doFilter(submit, report);
            //如果是状态2(发送失败)，就跳出循环
            if (report.getState() == 2) {
                break;
            }
        }
        //把report的状态同步到submit
        submit.setReportState(report.getState());
        //准备序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        //状态报告发送次数
        report.setSendCount(1);

        queueConfig.declareQueue(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT);
        //如果状态不是发送失败状态
        if (report.getState() != 2) {
            //序列化submit，将submit发送到网关队列
            oos.writeObject(submit);
            queueConfig.declareQueue(RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID());
            //发送待发日志
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID(), baos.toByteArray());

        } else {
            oos.writeObject(submit);
            queueConfig.declareQueue(RabbitMqConsants.TOPIC_SMS_SEND_LOG);
            //发送下发日志
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_SEND_LOG, baos.toByteArray());
        }
        oos.writeObject(report);
        //发送状态报告
        sendMessage.sendMessage(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, baos.toByteArray());
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
