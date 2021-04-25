package com.zengxin.homework;

import com.zengxin.homework.exception.DistributeLockException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 */
public interface DistributeLock {
    /**
     *
     * @param time 时间
     * @param timeUnit 单位
     * @return 是否获取锁成功
     * @throws DistributeLockException 异常
     */
    boolean tryLock(long time, TimeUnit timeUnit) throws DistributeLockException;

    void unlock();
}
