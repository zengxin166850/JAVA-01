package com.zengxin.homework0501.work1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JavaAopDemo implements InvocationHandler {

    public static void main(String[] args) {
        JavaAopDemo javaAop = new JavaAopDemo();
        IHello iHello = (IHello) javaAop.bind(new HelloImpl());
//        System.out.println("AOP代理后的实际类型：" + iHello.getClass());
        iHello.sayHello();
    }

    private Object delegate;

    public Object bind(Object delegate) {
        this.delegate = delegate;
        return Proxy.newProxyInstance(
                this.delegate.getClass().getClassLoader(), this.delegate
                        .getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            if ("sayHello".equals(method.getName())) {
                System.out.println("调用前...");
                result = method.invoke(this.delegate, args);
                System.out.println("调用后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
