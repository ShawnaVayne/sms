package com.qianfeng.smsplatform.userinterface.web;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.userinterface.exception.SmsInterfaceException;
import com.qianfeng.smsplatform.userinterface.service.SendStandard_Submit;
import com.qianfeng.smsplatform.userinterface.service.ValidService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 2019/12/39:31
 * <p>
 * 未知的事情 永远充满变数
 */
@WebServlet(name = "Servlet",value = "/smsInterface")
public class SmsServlet extends HttpServlet {

    @Autowired
    private ValidService validService;

    //@Autowired
    //private CacheFeignService cacheFeignService;

    @Autowired
    private SendStandard_Submit sendStandard_submit;





    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            long currenTime = System.currentTimeMillis(); // 获取当前时间

         List<Standard_Submit> list= null;
            //客户ip
            String userIpAddr = request.getRemoteAddr();
            //客户id
            String clientID = request.getParameter("clientID");
            //下行编号
            long srcID = Long.parseLong(request.getParameter("srcID"));
            //目的手机号
            String sd = request.getParameter("mobile");
            //获取消息内容
            String content = request.getParameter("content");
            //获取发送方密码
            String pwd = request.getParameter("pwd");

            String[] split = sd.split(",");
        try {
             list = validService.valid(request, response, split,content );

            for (Standard_Submit standardSubmit : list) {
                sendStandard_submit.setMessage(RabbitMqConsants.TOPIC_PRE_SEND, standardSubmit);
            }
        } catch (SmsInterfaceException e) {
            response.getWriter().write(e.getCode());
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
