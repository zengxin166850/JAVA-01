package com.zengxin.homework0501.work45;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * 输出方法执行耗时
 */
public class TimerAgent {
  public static void premain(String arguments,
                             Instrumentation instrumentation) {
    // aop 切入包含 fun 或 hello 的方法
    new AgentBuilder.Default()
            .type(ElementMatchers.nameContainsIgnoreCase("fun")
                    .or(ElementMatchers.nameContainsIgnoreCase("hello")))
            .transform((builder, type, classLoader, module) ->
                    builder.method(ElementMatchers.any())
                            .intercept(MethodDelegation.to(TimingInterceptor.class))
            ).installOn(instrumentation);
  }
}