package com.zengxin.tccdemo.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.zengxin.tccdemo.common.mapper")
@SpringBootApplication
public class HmilyAccountApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(HmilyAccountApplication.class).run(args);
    }
}
