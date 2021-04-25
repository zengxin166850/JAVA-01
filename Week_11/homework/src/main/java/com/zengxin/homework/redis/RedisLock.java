package com.zengxin.homework.redis;

import com.zengxin.homework.DistributeLock;
import com.zengxin.homework.SnowFlake;
import com.zengxin.homework.exception.DistributeLockException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * redis，锁实现
 */
public class RedisLock implements DistributeLock {

    /**
     * 存储拥有锁的线程映射关系
     */
    private final ConcurrentHashMap<Thread, String> threadData = new ConcurrentHashMap<>();
    /**
     * snowFlake
     */
    private SnowFlake snowFlake = new SnowFlake(1, 1);
    /**
     * jedisPool
     */
    private JedisPool jedisPool;

    /**
     * key
     */
    private String lockId;

    /**
     * 重试等待时间
     */
    private int retryAwait = 300;
    /**
     * 锁超时
     */
    private int lockTimeout = 2000;

    public RedisLock(JedisPool jedisPool, String lockId) {
        this.jedisPool = jedisPool;
        this.lockId = lockId;
    }

    @Override
    public boolean tryLock(long time, TimeUnit timeUnit) throws DistributeLockException {
        Objects.requireNonNull(timeUnit);
        Thread currentThread = Thread.currentThread();
        final String lockValue = tryRedisLock(time, timeUnit);
        if (lockValue != null) {
            threadData.put(currentThread, lockValue);
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        final Thread currentThread = Thread.currentThread();
        final String lockValue = threadData.get(currentThread);
        if (lockValue == null) {
            throw new DistributeLockException("你没有获取到锁权限：" + lockId);
        }
        try {
            unlockRedisLock(lockId, lockValue);
        } finally {
            threadData.remove(currentThread);
        }

    }

    private String tryRedisLock(long time, TimeUnit timeUnit) {
        final long startMillis = System.currentTimeMillis();
        final long millisToWait = timeUnit.toMillis(time);
        String lockValue = null;
        while (lockValue == null) {
            lockValue = createRedisKey(lockId);
            if (lockValue != null) {
                break;
            }
            if (System.currentTimeMillis() - startMillis - retryAwait > millisToWait) {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
        return lockValue;
    }

    /**
     * 使用 SET NX加锁, unique_value作为客户端唯一性的标识,解锁时需要用到
     * 这里复用之前用到的雪花算法来生成 lockValue
     * SET lock_key unique_value NX PX 10000
     *
     * @param lockId 锁的 key
     * @return lockValue  解锁时使用
     */
    private String createRedisKey(String lockId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String lockValue = lockId + snowFlake.nextId();
            /**
             * 不能直接使用setNx。jedis无法同时设置过期时间，所以采用lua的方式
             */
            String luaScript = ""
                    + "\nlocal r = tonumber(redis.call('SETNX', KEYS[1],ARGV[1]));"
                    + "\nredis.call('PEXPIRE',KEYS[1],ARGV[2]);"
                    + "\nreturn r";
            List<String> keys = new ArrayList<>();
            keys.add(lockId);
            List<String> args = new ArrayList<>();
            args.add(lockValue);
            args.add(lockTimeout + "");
            Long ret = (Long) jedis.eval(luaScript, keys, args);
            if (new Long(1).equals(ret)) {
                return lockValue;
            }
        }
        return null;
    }

    /**
     * 通过 lockValue 解锁
     *
     * @param key       key
     * @param lockValue value
     */
    void unlockRedisLock(String key, String lockValue) {
        try (Jedis jedis = jedisPool.getResource()) {
            String luaScript = ""
                    + "\nlocal v = redis.call('GET', KEYS[1]);"
                    + "\nlocal r= 0;"
                    + "\nif v == ARGV[1] then"
                    + "\nr =redis.call('DEL',KEYS[1]);"
                    + "\nend"
                    + "\nreturn r";
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(lockValue);
            jedis.eval(luaScript, keys, args);
        }
    }

}
