package com.zengxin.homework0501;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class Homework0501Application {

	public static void main(String[] args) {
		SpringApplication.run(Homework0501Application.class, args);
	}

}
