package com.zengxin.homework0502.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 不能使用spring-boot-maven-plugin插件进行打包否则会找不到类
 *
 */
@Configuration
@ConditionalOnClass(Student.class)
@ConditionalOnProperty(prefix = "student",matchIfMissing = true)
@EnableConfigurationProperties(Student.class)
public class DemoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Student student() {
        return new Student();
    }

    @Bean
    @ConditionalOnMissingBean
    public Klass klass() {
        return new Klass();
    }

    @Bean
    @ConditionalOnMissingBean
    public School school() {
        return new School();
    }
}