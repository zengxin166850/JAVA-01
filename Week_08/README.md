## 第 15  节课作业实践

#### 2、（必做）设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

1. 数据库初始化 [init.sql](./init.sql)；
2. 修改 `shardingsphere-proxy` 的配置文件 [config-sharding.yml](./config-sharding.yml) 并启动 `proxy`
3. 执行测试类中的代码 [Homework08ApplicationTests.java](./homework08/src/test/java/com/zengxin/homework08/Homework08ApplicationTests.java)

## 第 16  课作业实践

#### 1、（选做）列举常见的分布式事务，简单分析其使用场景和优缺点。



#### 2、（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。

#### 3、（选做）基于 ShardingSphere narayana XA 实现一个简单的分布式事务 demo。



#### 4、（选做）基于 seata 框架实现 TCC 或 AT 模式的分布式事务 demo。



#### 5、（选做☆）设计实现一个简单的 XA 分布式事务框架 demo，只需要能管理和调用2个 MySQL 的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。



#### 6、（选做☆）设计实现一个 TCC 分布式事务框架的简单 Demo，需要实现事务管理器，不需要实现全局事务的持久化和恢复、高可用等。



#### 7、（选做☆）设计实现一个 AT 分布式事务框架的简单 Demo，仅需要支持根据主键 id进行的单个删改操作的 SQL 或插入操作的事务





