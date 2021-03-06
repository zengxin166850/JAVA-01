package com.zengxin.homework0703;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zengxin.homework0703.mapper")
public class Homework0703Application {

	public static void main(String[] args) {
		SpringApplication.run(Homework0703Application.class, args);
	}

}
