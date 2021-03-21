package com.zengxin.tccdemo.consumer.controller;

import com.zengxin.tccdemo.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping(value = "/exchange")
    public Boolean exchange() {
        return consumerService.exchange();
    }

}
