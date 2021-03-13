package com.zengxin.tccdemo.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@EnableFeignClients
@ImportResource({"classpath:applicationContext.xml"})
@MapperScan("com.zengxin.tccdemo.common.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class HmilyInventoryApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HmilyInventoryApplication.class, args);
    }
}
