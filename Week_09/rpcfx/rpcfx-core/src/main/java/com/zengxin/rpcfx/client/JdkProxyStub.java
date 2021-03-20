package com.zengxin.rpcfx.client;

import java.lang.reflect.Proxy;

/**
 * 通过jdk生成代理
 */
@SuppressWarnings("unchecked")
public class JdkProxyStub implements StubFactory {
    @Override
    public <T> T createStub(Class<T> serviceClass, String url) {
        RpcfxInvocationHandler invocationHandler = new RpcfxInvocationHandler(serviceClass, url);
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, invocationHandler);
    }
}
