package com.qianfeng.smsplatform.userinterface.mq;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.userinterface.feign.CacheFeignService;
import com.qianfeng.smsplatform.userinterface.service.SendStandard_Submit;
import com.qianfeng.smsplatform.userinterface.util.Httpsend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2019/12/418:01
 * <p>
 * 未知的事情 永远充满变数
 */
@Component
//@RabbitListener(queues = RabbitMqConsants.TOPIC_PUSH_SMS_REPORT)
@Slf4j
public class MessageListener {

    @Autowired
    private CacheFeignService cacheFeignService;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private SendStandard_Submit sendStandard_submit;



    @RabbitListener(queues = RabbitMqConsants.TOPIC_PUSH_SMS_REPORT,autoStartup = "true")//指定当前方法是用于处理哪个队列的消息的,不需要在类上面添加注解
    public void reciveMessage(Standard_Report message) {
        System.out.println(message);

                System.err.println("收到了消息====>" + message);

        int s = message.getState();
        System.out.println("是否返回的状态:"+s);
        //根据isReturnStatus 判断是否返回
        if(s==0){
            System.out.println("不需要返回！");
            return;
        }

            //  需要返回 根据客户id查地址
            long clientID = message.getClientID();
            Map<Object, Object> objectObjectMap = cacheFeignService.hMGet(CacheConstants.CACHE_PREFIX_CLIENT + clientID);

            if(objectObjectMap==null||objectObjectMap.size()==0){
                log.info("用户不存在：103");
                 //throw new SmsInterfaceException("103","用户不存在：103");
            }
               log.info("缓存查出用户信息："+objectObjectMap);


                if(objectObjectMap==null){

                    log.info("缓存数据为空！", "234");
                    return;
                 }

                  String url = (String)objectObjectMap.get("receiveStatusUrl");
                  System.out.println(" 用户的url:"+url);


        executorService.execute(new Thread(){
            public void run(){

                try {
                    Httpsend.Send(url,message);
                   log.info("发送完成！");
                } catch (Exception e) {

                    if(message.getSendCount()<2){

                        log.info("第一次");
                        sendStandard_submit.sendstatus(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, message);
                        message.setSendCount(2);
                        log.info("第二次开始");
                        return;
                    }
                        sendStandard_submit.sendstatus(RabbitMqConsants.TOPIC_SMS_REPORT_FAILURE, message);
                    log.info("失败2次状态发往失败MQ");
                    e.printStackTrace();
                }


        }


       });


}


}
