package com.zengxin.rpcfx.netty.client;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

/**
 * 通过 ByteBuddy 生成代理
 */
@SuppressWarnings("unchecked")
public class ByteBuddyProxyStub implements StubFactory {
    @Override
    public <T> T createStub(Class<T> serviceClass, String url) {
        try {
            return (T) new ByteBuddy().subclass(Object.class)
                    .implement(serviceClass)
                    //实际调用的还是 RpcfxInvocationHandler
                    .intercept(InvocationHandlerAdapter.of(new RpcfxInvocationHandler(serviceClass, url)))
                    .make()
                    .load(ByteBuddyProxyStub.class.getClassLoader())
                    .getLoaded()
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
