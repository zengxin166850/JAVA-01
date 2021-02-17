package com.zengxin.homework0501.work41.config;

import com.zengxin.homework0501.work41.inbound.HttpInboundServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NettyConfiguration {

    @Autowired
    HttpInboundServer httpInboundServer;

    @PostConstruct
    public void server() throws Exception {
        httpInboundServer.run();
    }

}
