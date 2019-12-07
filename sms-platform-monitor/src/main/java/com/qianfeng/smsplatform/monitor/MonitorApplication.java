package com.qianfeng.smsplatform.monitor;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.monitor.feign.CacheFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MonitorApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(MonitorApplication.class);
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonitorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("项目启动~~~");
    }

}

