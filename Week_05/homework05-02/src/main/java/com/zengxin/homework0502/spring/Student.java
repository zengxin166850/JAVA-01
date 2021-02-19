package com.zengxin.homework0502.spring;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "student")
public class Student implements Serializable {

    private int id;
    private String name;
    
    public void init(){
        System.out.println("hello...........");
    }
    
}
