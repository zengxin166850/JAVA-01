package com.zengxin.rpcfx.demo.provider;

import com.zengxin.rpcfx.annotation.RPCService;
import com.zengxin.rpcfx.api.ServiceProviderDesc;
import com.zengxin.rpcfx.netty.client.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Map;

@Component
public class RPCServiceListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RPCServiceListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(RPCService.class);
        if (beans.size() > 0) {
            try {
                CuratorFramework client = CuratorClient.getClient("localhost:2181");
                for (Object bean : beans.values()) {
                    Class<?>[] interfaces = bean.getClass().getInterfaces();
                    //限制接口数必须为1
                    if(interfaces.length!=1){
                        throw new RuntimeException("register failed, rpc service's interfaces must be 1");
                    }
                    registerService(client, interfaces[0].getName());
                }
                logger.info("=====register {} service into zookeeper success=====", beans.size());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("register service failed:{}", e.getMessage());
            }
        }
    }

    private static void registerService(CuratorFramework client, String service) throws Exception {
        ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
                .host(InetAddress.getLocalHost().getHostAddress())
                .port(8080).serviceClass(service).build();
        // String userServiceSescJson = JSON.toJSONString(userServiceSesc);

        try {
            if (null == client.checkExists().forPath("/" + service)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath("/" + service, "service".getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        client.create().withMode(CreateMode.EPHEMERAL).
                forPath("/" + service + "/" + userServiceSesc.getHost() + "_" + userServiceSesc.getPort(), "provider".getBytes());
    }
}