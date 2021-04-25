package com.zengxin.homework;

import com.zengxin.homework.redis.RedisLock;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RedisLockTest {

    @Test
    public void testTry() throws InterruptedException {
        JedisPool jedisPool = new JedisPool("172.25.17.87",6379);
        RedisLock redisLock = new RedisLock(jedisPool,"testLock");
        int size = 100;
        final CountDownLatch startCountDownLatch = new CountDownLatch(1);
        final CountDownLatch endDownLatch=new CountDownLatch(size);
        for (int i =0;i<size;i++){
            new Thread() {
                public void run() {
                    try {
                        startCountDownLatch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    final int sleepTime= ThreadLocalRandom.current().nextInt(2)*1000;
                    final boolean getLock = redisLock.tryLock(1100, TimeUnit.MICROSECONDS);
                    if(getLock){
                        System.out.println(Thread.currentThread().getName() + ":getLock");
                        try {
                            sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + ":sleeped:"+sleepTime);
                        endDownLatch.countDown();
                        redisLock.unlock();
                    }else{
                        System.out.println(Thread.currentThread().getName() + ":timeout");
                        endDownLatch.countDown();
                    }
                }
            }.start();
        }
        startCountDownLatch.countDown();
        endDownLatch.await();
    }
}
