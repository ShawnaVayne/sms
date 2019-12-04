package com.qianfeng.smsplatform.webmaster.controller;

<<<<<<< HEAD
=======
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
>>>>>>> 024f677c8cc4f360b9122178d7a08bd08d212780
import com.qianfeng.smsplatform.webmaster.dto.SmsDTO;
import com.qianfeng.smsplatform.webmaster.util.R;
<<<<<<< HEAD
=======
import com.qianfeng.smsplatform.webmaster.util.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> 024f677c8cc4f360b9122178d7a08bd08d212780
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

<<<<<<< HEAD
=======
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

>>>>>>> 024f677c8cc4f360b9122178d7a08bd08d212780
@Controller
public class SmsController {
    private Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @RequestMapping("/sys/sms/save")
<<<<<<< HEAD
    public R addBlack(@RequestBody SmsDTO smsDTO){
=======
    public R addBlack(@RequestBody SmsDTO smsDTO,HttpSession session) throws JsonProcessingException {
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
            //String subJson = objectMapper.writeValueAsString(submit);
            rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_PRE_SEND,submit);
        }
>>>>>>> 024f677c8cc4f360b9122178d7a08bd08d212780

        return R.ok();
    }

}
