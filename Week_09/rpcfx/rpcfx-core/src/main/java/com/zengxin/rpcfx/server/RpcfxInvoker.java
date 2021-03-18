package com.zengxin.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zengxin.rpcfx.api.RpcfxRequest;
import com.zengxin.rpcfx.api.RpcfxResolver;
import com.zengxin.rpcfx.api.RpcfxResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker<T> {

    private RpcfxResolver<T> resolver;

    public RpcfxInvoker(RpcfxResolver<T> resolver){
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        try {
            // 作业1：改成泛型和反射
            Class<T> serviceClz = (Class<T>) Class.forName(serviceClass);
            T service = resolver.resolve(serviceClz);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch ( Exception e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}