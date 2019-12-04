package com.qianfeng.smsplatform.userinterface.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.userinterface.exception.SmsInterfaceException;
import com.qianfeng.smsplatform.userinterface.feign.CacheFeignService;
import com.qianfeng.smsplatform.userinterface.service.ValidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2019/12/314:08
 * <p>
 * 未知的事情 永远充满变数
 */
@Service
public class ValidServiceImpl implements ValidService {

     @Autowired
      private CacheFeignService cacheFeignService;


    @Override
    public List<Standard_Submit> valid(HttpServletRequest request, HttpServletResponse response, String[] split,String content,String userIpAddr,String pwd,String clientID) throws IOException {


        List<Standard_Submit> list = new ArrayList<>();

        Standard_Submit standard_submit = new Standard_Submit();


        if(split.length<=99){

        for (int i = 0; i < split.length; i++) {
            Map<Object, Object> objectObjectMap = cacheFeignService.hMGet(CacheConstants.CACHE_PREFIX_CLIENT+clientID);

            String pwd1 = (String)objectObjectMap.get("pwd");
            String ip1 = (String)objectObjectMap.get("ipAddress");
            System.out.println("查出数据："+objectObjectMap+"----------------");
            System.out.println(userIpAddr+"=====查询出的：===="+ip1);
            if(objectObjectMap==null||objectObjectMap.size()==0){
                throw new SmsInterfaceException("103","用户不存在：103");
            }
           /* else if(!ip1.equals(userIpAddr)){
                throw new SmsInterfaceException("103","ip错误：103");
            }*/
            else if(!pwd.equals(pwd1)){
                throw new SmsInterfaceException("101","密码错误：101");
            }
            else if (split == null || content == null) {
                standard_submit.setErrorCode("104");
                throw new SmsInterfaceException("105","手机号或内容为空：104");

            } else {
                //定义手机号的规则
                String regex = "1[357]\\d{9}";


                System.out.println(split);
                if (split[i].matches(regex)) {
                    //成功后继续执行
                    //验证内容长度
                    System.out.println("手机号："+split[i]);
                    if (content.length() < 500) {
                        standard_submit.setDestMobile(split[i]);
                        standard_submit.setMessageContent(content);
                        standard_submit.setClientID(Integer.parseInt(clientID));
                        standard_submit.setSource(1);
                        list.add(standard_submit);
                    } else {
                        throw new SmsInterfaceException("104","内容超长：104");

                    }
                } else {
                    throw new SmsInterfaceException("105","手机号验证失败：105");

                }
            }
        }
        }else {
            throw new SmsInterfaceException("105","手机号过多！：105");
        }
                return list;
    }
}