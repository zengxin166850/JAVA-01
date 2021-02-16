package com.zengxin.homework0501.work2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanDemo {
    public static void main(String[] args) {
        //Xml配置
        ApplicationContext context1 = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student100 = (Student) context1.getBean("student100");
        System.err.println(student100.toString());

        //注解配置
        ApplicationContext context2 = new AnnotationConfigApplicationContext("com.zengxin.homework0501.work2");
        Student student101 = (Student) context2.getBean("student101");
        System.err.println(student101.toString());
    }
}
