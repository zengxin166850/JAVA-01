package com.zengxin.homework0501.work44;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyDemo {

    @Log
    public int foo(int value) {
        System.out.println("foo: " + value);
        return value;
    }

    public int bar(int value) {
        System.out.println("bar: " + value);
        return value;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ByteBuddyDemo demo = new ByteBuddy()
                .subclass(ByteBuddyDemo.class)
                //byteBuddydemo下的所有方法
                .method(ElementMatchers.any())
                //aop 实现细节在 LoggerAdvisor 中
                .intercept(Advice.to(LoggerAdvisor.class))
                .make()
                .load(ByteBuddyDemo.class.getClassLoader())
                .getLoaded()
                .newInstance();
        demo.bar(123);
        demo.foo(456);
    }
}
