package com.zengxin.homework.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCounter {

    private String key;
    private JedisPool jedisPool;

    public RedisCounter(JedisPool jedisPool, String key) {
        this.jedisPool = jedisPool;
        this.key = key;
    }

    public void initCount(int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, count + "");
        }
    }

    public void increase() {
        String luaScript = ""
                + "\nlocal v = redis.call('GET', KEYS[1]);"
                + "\nlocal r= 0;"
                + "\nif v == ARGV[1] then"
                + "\nr =redis.call('DEL',KEYS[1]);"
                + "\nend"
                + "\nreturn r";
    }

    public void decrease() {
        String luaScript = ""
                + "\nlocal v = redis.call('GET', KEYS[1]);"
                + "\nlocal r= 0;"
                + "\nif v == ARGV[1] then"
                + "\nr =redis.call('DEL',KEYS[1]);"
                + "\nend"
                + "\nreturn r";
    }
}
