package com.zengxin.rpcfx.netty.client;

import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过cglib生成代理
 */
@SuppressWarnings("unchecked")
public class CglibProxyStub implements StubFactory {
    private final Logger logger = LoggerFactory.getLogger(CglibProxyStub.class);

    @Override
    public <T> T createStub(Class<T> serviceClass, String url) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new RpcfxInvocationHandler(serviceClass, url));
        enhancer.setSuperclass(serviceClass);
        logger.info("cglib proxy created for class: {}", serviceClass.getName());
        return (T) enhancer.create();
    }
}
