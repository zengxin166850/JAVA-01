package com.zengxin.rpcfx.demo.provider;

import com.zengxin.rpcfx.api.RpcfxResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DemoResolver<T> implements RpcfxResolver<T>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object resolve(String serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }

    @Override
    public T resolve(Class<T> serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }

}
