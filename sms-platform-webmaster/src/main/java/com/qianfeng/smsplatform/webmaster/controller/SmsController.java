package com.qianfeng.smsplatform.webmaster.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.webmaster.dto.SmsDTO;
import com.qianfeng.smsplatform.webmaster.pojo.TAdminUser;
import com.qianfeng.smsplatform.webmaster.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import java.util.Date;
import java.util.UUID;


@Controller
public class SmsController {
    private Logger logger = LoggerFactory.getLogger(SmsController.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @RequestMapping("/sys/sms/save")
    public R addBlack(@RequestBody SmsDTO smsDTO, HttpSession session) throws JsonProcessingException {
        String mobile = smsDTO.getMobile();
        String content = smsDTO.getContent();
        //获取clientID
        TAdminUser user = (TAdminUser) session.getAttribute("userInfo");
        Integer clientid = user.getClientid();
        if(mobile==null||"".equalsIgnoreCase(mobile.trim())){
            return R.error("手机号不能为空~");
        }else if(content==null||"".equalsIgnoreCase(content.trim())){
            return R.error("发送内容不能为空~");
        }
        String[] mobiles=mobile.split("\\n");
        logger.info("分割后的手机号数组为：{}", Arrays.asList(mobiles));

        for (int i = 0; i < mobiles.length; i++) {

            Standard_Submit submit = new Standard_Submit();
            submit.setClientID(clientid);
            submit.setMessageContent(content);
            submit.setDestMobile(mobiles[i]);
            submit.setSource(2);
            submit.setSendTime(new Date());
            submit.setMsgid(String.valueOf(UUID.randomUUID()));
            //String subJson = objectMapper.writeValueAsString(submit);
            rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_PRE_SEND,submit);
        }

        return R.ok();
    }

}
