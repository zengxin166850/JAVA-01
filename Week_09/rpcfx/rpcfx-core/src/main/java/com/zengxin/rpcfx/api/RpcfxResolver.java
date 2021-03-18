package com.zengxin.rpcfx.api;

public interface RpcfxResolver<T> {

    Object resolve(String serviceClass);

    T resolve(Class<T> serviceClass);

}