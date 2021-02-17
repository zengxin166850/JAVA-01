package com.zengxin.homework0501.work41.config;

import com.zengxin.homework0501.work41.outbound.NamedThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfiguration {


    @Bean("inboundThreadPool")
    public ExecutorService inboundThreadPool() {
        return new ThreadPoolExecutor(
                Integer.parseInt(inboundCoreSize), Integer.parseInt(inboundMaxSize),
                Integer.parseInt(inboundAliveSec), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(Integer.parseInt(inboundQueueCap)),
                new NamedThreadFactory("inboundThread"));
    }

    @Bean("outboundThreadPool")
    public ExecutorService outboundThreadPool() {
        return new ThreadPoolExecutor(
                Integer.parseInt(outboundCoreSize), Integer.parseInt(outboundMaxSize),
                Integer.parseInt(outboundAliveSec), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(Integer.parseInt(outboundQueueCap)),
                new NamedThreadFactory("outboundThread"));
    }


    @Value("${inbound.corePoolSize}")
    private String inboundCoreSize;
    @Value("${inbound.maxPoolSize}")
    private String inboundMaxSize;
    @Value("${inbound.keepAliveSeconds}")
    private String inboundAliveSec;
    @Value("${inbound.queueCapacity}")
    private String inboundQueueCap;

    @Value("${outbound.corePoolSize}")
    private String outboundCoreSize;
    @Value("${outbound.maxPoolSize}")
    private String outboundMaxSize;
    @Value("${outbound.keepAliveSeconds}")
    private String outboundAliveSec;
    @Value("${outbound.queueCapacity}")
    private String outboundQueueCap;
}
