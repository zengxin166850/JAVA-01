package com.zengxin.homework0501.work2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfiguration {
    @Bean
    public Student student101() {
        return new Student(101, "KK101");
    }
}
