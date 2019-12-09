package com.qianfeng.smsplatform.userinterface.util;

import com.alibaba.fastjson.JSON;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 2019/12/419:09
 * <p>
 * 未知的事情 永远充满变数
 */
@Slf4j
public class Httpsend {

  public   static  void Send(String url, Standard_Report standard_report){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String jsonString=null;


        try {
             jsonString = JSON.toJSONString(standard_report);



            log.info("返回给客户端的对象："+jsonString);







        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);
        log.info("执行完成！");

        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");


        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            // System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                //  System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                  System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(" ",e );
        } catch (ParseException e) {
            throw new RuntimeException(" ",e );
        } catch (IOException e) {
            throw new RuntimeException(" ",e );
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





    }



