package com.qianfeng.smsplatform.monitor.utils;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Slf4j
public class ConnectionUtil {

    public static Connection getConnection(final String host,
                                           final Integer port,
                                           final String username,
                                           final String password,
                                           final String virtualHost) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);//用户名
        connectionFactory.setPassword(password);//密码
        connectionFactory.setVirtualHost(virtualHost);//设置访问哪个数据库
        return connectionFactory.newConnection();
    }
}
