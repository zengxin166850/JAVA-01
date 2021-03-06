## 第 13 节课作业实践

测试表较为简单，仅涉及6个字段

#### 2、（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

- 使用单线程循环插入，需要10分钟以上；
- 单线程，开启 `prepareStatement` ，每次批量提交，`values(1,1,1),(2,2,2)` 的方式需要 90s 左右；
- 使用存储过程拼接 `values` 串再提交，时长约 42s ：
- 单线程，在连接串中添加`rewriteBatchedStatements=true`之后 ,执行时长约 `8s`; [BatchInsertDemo.java](./homework0701/src/main/java/com/zengxin/homework0701/BatchInsertDemo.java)
- 使用 `hikari` 数据库连接池多线程执行，同时开启批量参数后，约2s；[Homework0701ApplicationTests.java](./homework0701/src/test/java/com/zengxin/homework0701/Homework0701ApplicationTests.java)



### 思维导图

![](./13课-mysql事务与锁.png)

## 第 14 节课作业实践
#### 2、（必做）读写分离-动态切换数据源版本1.0

- 最简单的切换可以使用AOP判断方法名前缀、后缀，并手动设置数据源
- `AbstractRoutingDataSource`统一管理外加自定义的注解简化了切换

 [Homework0702ApplicationTests.java](./homework0702/src/test/java/com/zengxin/homework0702/Homework0702ApplicationTests.java)

#### 3、（必做）读写分离-数据库框架版本2.0

配置文件位置：[application.yml](./homework0703/src/main/resources/application.yml)

测试类位置： [Homework0703ApplicationTests.java](./homework0703/src/test/java/com/zengxin/homework0703/Homework0703ApplicationTests.java)

> - `shardingjdbc`可以方便的使用全配置方式动态管理数据源，无代码侵入。支持多个从节点的负载均衡策略配置，非常灵活
>
> - 集成 `shardingjdbc` 后，需要注意 `sql`语句的写法，不支持的地方需参照文档，如不支持`union`、`prepareStatement`等， `insert`、`select` 语句中需带上字段名,否则会有解析、映射异常

