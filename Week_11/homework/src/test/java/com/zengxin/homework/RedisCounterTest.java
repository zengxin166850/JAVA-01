package com.zengxin.homework;

import com.zengxin.homework.redis.RedisCounter;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.concurrent.CountDownLatch;

/**
 * 模拟减库存
 */
public class RedisCounterTest {

    @Test
    public void testTry() throws InterruptedException {
        JedisPool jedisPool = new JedisPool("172.25.17.87", 6379);
        RedisCounter redisCounter = new RedisCounter(jedisPool, "inventory", "10");
        int size = 100;
        final CountDownLatch startCountDownLatch = new CountDownLatch(1);
        final CountDownLatch endDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            new Thread() {
                public void run() {
                    try {
                        startCountDownLatch.await();
                        final Long result = redisCounter.decreaseInventory();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (JedisDataException e) {
                        //
                        if (e.getMessage().contains("inventory not enough")) {
                            System.out.println("秒杀失败...");
                        } else {
                            throw new RuntimeException("扣减库存出错");
                        }

                    }
                    endDownLatch.countDown();
                }
            }.start();
        }
        startCountDownLatch.countDown();
        endDownLatch.await();
    }

    @Test
    public void testGetInventory() {
        JedisPool jedisPool = new JedisPool("172.25.17.87", 6379);
        final Jedis jedis = jedisPool.getResource();
        final String inventory = jedis.get("inventory");
        System.out.println(inventory);
    }
}
