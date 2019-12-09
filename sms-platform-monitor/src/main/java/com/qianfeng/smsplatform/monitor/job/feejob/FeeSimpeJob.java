package com.qianfeng.smsplatform.monitor.job.feejob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.monitor.feign.CacheFeign;
import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import com.qianfeng.smsplatform.monitor.pojo.TClientBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;

import java.lang.invoke.ConstantCallSite;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * @Classname FeeSimpeJob
 * @Description TODO
 * @Date 2019/12/6 10:10
 * @Created by sunjiangwei
 */
@Slf4j
public class FeeSimpeJob implements SimpleJob {

    @Autowired
    private CacheFeign cacheFeign;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ChannelFeign channelFeign;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${alert.fee}")
    private Long fee;

    @Override
    public void execute(ShardingContext shardingContext) {

        Set<String> keys = cacheFeign.keys(CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + "*");
        System.err.println(keys);
        for (String key : keys) {
            long clientId = Long.valueOf(key.substring(key.indexOf(":")+1));
            //调用服务，查询对应客户信息
            TClientBusiness client = channelFeign.findClientBusinessById(clientId);
            //log.info("用户：{}",client.getMobile());
            String str = cacheFeign.get(key);
            Integer exfee = Integer.valueOf(str);
            //log.info("剩余余额：{}",exfee);
            if (exfee < fee){
                //封装信息对象
                Standard_Submit submit = new Standard_Submit();
                submit.setClientID((int) clientId);
                submit.setDestMobile(client.getMobile());
                submit.setSendTime(new Date());
                submit.setSource(2);
                String msg = "尊敬的用户"+clientId+"【账号余额"+exfee+"厘】===>温馨提示：您的余额少于100元，请您及时充值,以免给您造成不便 ;) ;)";
                submit.setMessageContent(msg);

                amqpTemplate.convertAndSend(RabbitMqConsants.TOPIC_SMS_SEND_LOG,submit);
                System.err.println(msg);
            }
        }
    }
}
