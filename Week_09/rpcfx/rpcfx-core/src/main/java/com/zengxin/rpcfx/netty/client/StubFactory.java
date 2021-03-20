package com.zengxin.rpcfx.netty.client;

/**
 * 生成rpc接口的代理，（stub）
 */
public interface StubFactory {

    //生成代理类
     <T> T createStub(Class<T> serviceClass, String url);
}
