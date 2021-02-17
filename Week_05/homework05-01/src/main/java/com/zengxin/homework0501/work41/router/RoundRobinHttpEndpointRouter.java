package com.zengxin.homework0501.work41.router;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinHttpEndpointRouter implements HttpEndpointRouter {
    //原子
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String route(List<String> endpoints) {
        int result = counter.getAndIncrement();
        int index = result % endpoints.size();
        String serverName = endpoints.get(index);
        System.out.println("本次访问的服务为：" + serverName);
        return serverName;
    }

}
