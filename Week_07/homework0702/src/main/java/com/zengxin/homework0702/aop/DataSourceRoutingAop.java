package com.zengxin.homework0702.aop;

import com.zengxin.homework0702.config.DBContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 路由aop
 */
@Aspect
@Component
public class DataSourceRoutingAop {

    @Pointcut("@annotation(com.zengxin.homework0702.aop.ReadOnly) ")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.zengxin.homework0702.aop.WriteOnly) ")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

    @After("writePointcut()|| readPointcut()")
    public void reset() {
        DBContextHolder.reset();
    }


    /**
     * 依方法名判断
     */
//    @Before("execution(* com.zengxin.homework0702.service.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }
}