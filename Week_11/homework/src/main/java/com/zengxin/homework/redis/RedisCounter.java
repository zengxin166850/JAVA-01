package com.zengxin.homework.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * redis 计数器
 */
public class RedisCounter {

    /**
     * redis_key
     */
    private String key;
    /**
     * 初始化 count
     */
    private String count;
    /**
     * jedis连接池
     */
    private JedisPool jedisPool;

    /**
     * 构造并初始化
     *
     * @param jedisPool jedis连接池
     * @param key       key
     * @param count     count
     */
    public RedisCounter(JedisPool jedisPool, String key, String count) {
        this.jedisPool = jedisPool;
        this.count = count;
        this.key = key;
        init();
    }

    public void init() {
        Objects.requireNonNull(jedisPool);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, count + "");
        }
    }

    /**
     * 原子自增
     *
     * @return count
     */
    public Long increase() {
        try (final Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        }
    }

    /**
     * 递减
     *
     * @return 库存
     */
    public Long decrease() {
        try (final Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(key);
        }

    }

    /**
     * 库存扣减
     *
     * @return 库存
     */
    public Long decreaseInventory() {
        try (final Jedis jedis = jedisPool.getResource()) {
            String luaScript = ""
                    + "\nlocal inventory = redis.call('GET', KEYS[1]);"
                    + "\nlocal r = 0;"
                    + "\nif tonumber(inventory) == 0 then"
                    + "\nerror \"inventory not enough\""
                    + "\nelse"
                    + "\nr =redis.call('DECR',KEYS[1]);"
                    + "\nend"
                    + "\nreturn r";
            List<String> args = new ArrayList<>();
            args.add(key);
            final Long r = (Long) jedis.eval(luaScript, args, new ArrayList<>());
            System.out.println("curr inventory:" + r + " " + Thread.currentThread() + ": 秒杀成功...");
            return r;
        }
    }


}
