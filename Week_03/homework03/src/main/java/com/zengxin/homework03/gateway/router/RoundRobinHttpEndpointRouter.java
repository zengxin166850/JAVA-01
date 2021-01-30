package com.zengxin.homework03.gateway.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinHttpEndpointRouter implements HttpEndpointRouter {
    //原子
    private AtomicInteger counter = new AtomicInteger(0);
    private static RoundRobinHttpEndpointRouter instance = new RoundRobinHttpEndpointRouter();

    //模拟一下单例
    private RoundRobinHttpEndpointRouter() {

    }

    @Override
    public String route(List<String> endpoints) {
        int result = counter.getAndIncrement();
        int index = result % endpoints.size();
        String serverName = endpoints.get(index);
        System.out.println("本次访问的服务为：" + serverName);
        return serverName;
    }

    public static RoundRobinHttpEndpointRouter getInstance() {
        return instance;
    }
}
