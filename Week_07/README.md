## 第 13 节课作业实践

```
DROP TABLE IF EXIESTS t_order;

CREATE TABLE `t_order` (
  `id` int(11) NOT NULL COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '总价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### 2、（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

- 使用单线程循环插入，需要10分钟以上；
- 单线程，开启 `prepareStatement` ，每次批量提交，`values(1,1,1),(2,2,2)` 的方式需要 90s 左右；
- 使用存储过程拼接 `values` 串再提交，时长约 42s ：
- 单线程，在连接串中添加`rewriteBatchedStatements=true`之后，再使用 `prepareStatemtn` ,执行时长约 `8s`;
- 使用数据库连接池，多线程执行，同时开启批量参数后，约2s；



### 思维导图

![](./13课-mysql事务与锁.png)

## 第 14 节课作业实践
#### 1、（选做）配置一遍异步复制，半同步复制、组复制。

是的

#### 2、（必做）读写分离-动态切换数据源版本1.0

 测试类 -- >  Homework0702ApplicationTests

#### 3、（必做）读写分离-数据库框架版本2.0



#### 4、（选做）读写分离-数据库中间件版本3.0

是的 

#### 5、（选做）配置 MHA，模拟 master 宕机

暗示

#### 6、（选做）配置 MGR，模拟 master 宕机

 阿斯达

#### 7、（选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构

 