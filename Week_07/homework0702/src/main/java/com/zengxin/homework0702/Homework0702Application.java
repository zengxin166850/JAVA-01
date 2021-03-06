package com.zengxin.homework0702;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zengxin.homework0702.mapper")
public class Homework0702Application {

    public static void main(String[] args) {
        SpringApplication.run(Homework0702Application.class, args);
    }

}
