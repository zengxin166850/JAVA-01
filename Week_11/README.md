# 第 22 课作业实践

### 实现一个分布式锁

#### 加锁

总体思路：使用SET NX 指令，并指定过期时间

```
SET lock_key unique_value NX PX 10000
```

- unique_value 为唯一值，用于判断加锁的客户端是谁
- PX 代表过期时间

由于 jedis 不提供带过期时间的 setnx 方法，故采用如下的 lua 脚本进行实现

```
local r = tonumber(redis.call('SETNX', KEYS[1],ARGV[1]));
redis.call('PEXPIRE',KEYS[1],ARGV[2]);
return r
```

加锁成功后，将成功获取到锁的线程信息和 unique_value 保存下来。解锁时需要用到 unique_value，伪代码如下：

```
private final ConcurrentHashMap<Thread, String> threadData = new ConcurrentHashMap<>();
.......
final String lockValue = tryRedisLock(time, timeUnit);
if (lockValue != null) {
    threadData.put(currentThread, lockValue);
    return true;
}
```

#### 解锁

总体思路：通过加锁时留下的唯一值 unique_value 进行解锁，避免错误释放了其它线程的锁，由于查询和删除 key 是两步操作不具有原子性，故采用如下的 lua 脚本实现

```

//释放锁 比较unique_value是否相等，避免误释放
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```



### 实现分布式计数器，模拟减库存

整体思路：使用 redis 的原子增减指令实现

```
INCR/DECR key;
```

减库存通过一下的 lua 脚本判断

```
//获取ip对应的访问次数
local inventory = redis.call('GET', KEYS[1]);
//如果超过访问次数超过20次，则报错
IF inventory = 0 THEN
    ERROR "库存不足！！！"
ELSE
    redis.call('DECR', KEYS[1]);
    //执行其他操作
    DO THINGS
END
```

