package com.zengxin.rpcfx.demo.provider;

import com.zengxin.rpcfx.demo.api.OrderService;
import com.zengxin.rpcfx.demo.api.UserService;
import com.zengxin.rpcfx.netty.server.RpcNettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RpcfxServerApplication implements ApplicationRunner {
    @Autowired
    RpcNettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nettyServer.start();
    }

    public static void main(String[] args) throws Exception {
        // 进一步的优化，是在spring加载完成后，从里面拿到特定注解的bean，自动注册到zk
        SpringApplication.run(RpcfxServerApplication.class, args);
    }

    @Bean
    public RpcNettyServer nettyServer(ApplicationContext applicationContext) {
        return new RpcNettyServer(8080, applicationContext);
    }

    @Bean
    public UserService createUserService() {
        return new UserServiceImpl();
    }

    @Bean
    public OrderService createOrderService() {
        return new OrderServiceImpl();
    }
}
