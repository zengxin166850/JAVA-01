package com.zengxin.homework0702.config;

import com.zengxin.homework0702.consts.DBTypeEnum;

import java.util.concurrent.atomic.AtomicInteger;

public class DBContextHolder {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private DBContextHolder() {
    }

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();


    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        System.out.println("当前数据源为master");
    }

    public static void slave() {
        //  轮询负载均衡
        int index = counter.getAndIncrement() % 2;
        if (counter.get() > 10000) {
            counter.set(0);
        }
        if (index == 0) {
            set(DBTypeEnum.SLAVE);
            System.out.println("当前数据源为slave1");
        } else {
            set(DBTypeEnum.SLAVE);
            System.out.println("当前数据源为slave2");
        }
    }

    public static void reset() {
        contextHolder.remove();
    }
}