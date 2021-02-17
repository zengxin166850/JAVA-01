package com.zengxin.homework0501.work41.config;

import com.zengxin.homework0501.work41.router.HttpEndpointRouter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Aspect
public class AopConfiguration {

    @Value("${proxyServers}")
    private List<String> backendUrls;

    @Autowired
    HttpEndpointRouter roundRobinHttpEndpointRouter;

    /**
     * filter切面
     */
    @Pointcut("@annotation(com.zengxin.homework0501.work41.config.FilterAop)")
    private void filterPointcut() {
    }

    /**
     * router切面
     */
    @Pointcut("@annotation(com.zengxin.homework0501.work41.config.RouterAop)")
    private void routerPointcut() {
    }

    /**
     * 添加 header
     *
     * @param joinPoint 切点
     */
    @Around(value = "filterPointcut()")
    public void adviceFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        //inbound filter
        if ("channelRead".equals(methodName)) {
//            System.out.println("开始inbound filter增强");
            FullHttpRequest fullRequest = (FullHttpRequest) args[1];
            fullRequest.headers().set("mao", "soul");
        }
        //outbound filter
        if ("handleResponse".equals(methodName)) {
//            System.out.println("开始outbound filter增强");
            FullHttpResponse response = (FullHttpResponse) args[2];
            response.headers().set("kk", "java-1-nio");
        }
        joinPoint.proceed(args);
    }

    /**
     * random 随机路由
     *
     * @param joinPoint 切点
     */
    @Around(value = "routerPointcut()")
    public void adviceRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        //随机一个server

        int size = backendUrls.size();
        Random random = new Random(System.currentTimeMillis());
        String backendUrl = backendUrls.get(random.nextInt(size));
        System.out.println("本次路由地址为：" + backendUrl);
        Object[] args = joinPoint.getArgs();
        FullHttpRequest fullRequest = (FullHttpRequest) args[0];
        args[2] = backendUrl + fullRequest.uri();
        //执行远程调用
        joinPoint.proceed(args);
    }

}
