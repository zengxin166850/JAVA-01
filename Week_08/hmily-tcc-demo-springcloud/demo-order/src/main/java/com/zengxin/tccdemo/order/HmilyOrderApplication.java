package com.zengxin.tccdemo.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@ImportResource({"classpath:applicationContext.xml"})
@MapperScan("com.zengxin.tccdemo.common.mapper")
@SpringBootApplication
public class HmilyOrderApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HmilyOrderApplication.class, args);
    }
}
