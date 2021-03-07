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

#### 4、（选做）读写分离-数据库中间件版本3.0

测试结果：（测试类见 [Homework0704ApplicationTests.java](./homework0704/src/test/java/com/zengxin/homework0704/Homework0704ApplicationTests.java)）

> timestamp字段在proxy后，会返回带有 T 的类型 2021-03-07T15:21:26  导致映射失败。[issues/9544](https://github.com/apache/shardingsphere/issues/9544),尚未验证新版，
>
> 5.0.0-alpha版本在 `show tables` 时会有 `getConnection` 问题,主分支已修复[issues/8525](https://github.com/apache/shardingsphere/issues/8525)

```
#原始写sql
[INFO ] 15:03:37.949 [ShardingSphere-Command-6] ShardingSphere-SQL - Logic SQL: insert into t_order(id,user_id,product_id,price,create_time,pay_time) values(3,3,3,3.0,'2021-03-07 07:03:36.606','2021-03-07 07:03:36.606')
[INFO ] 15:03:37.950 [ShardingSphere-Command-6] ShardingSphere-SQL - SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
#实际发往了主节点 primary_ds
[INFO ] 15:03:37.954 [ShardingSphere-Command-6] ShardingSphere-SQL - Actual SQL: primary_ds ::: insert into t_order(id,user_id,product_id,price,create_time,pay_time) values(3,3,3,3.0,'2021-03-07 07:03:36.606','2021-03-07 07:03:36.606')

-------------------
#查询语句
[INFO ] 15:23:16.515 [ShardingSphere-Command-1] ShardingSphere-SQL - Logic SQL: select id,user_id,product_id,price from t_order where id = 3
#实际第一次发往了replica_ds_1
[INFO ] 15:23:16.516 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: replica_ds_1 ::: select id,user_id,product_id,price from t_order where id = 3
[INFO ] 15:23:16.543 [ShardingSphere-Command-0] ShardingSphere-SQL - Logic SQL: select id,user_id,product_id,price from t_order where id = 4

#第二次发往了replica_ds_0
[INFO ] 15:23:16.544 [ShardingSphere-Command-0] ShardingSphere-SQL - Actual SQL: replica_ds_0 ::: select id,user_id,product_id,price from t_order where id = 4
```



##### 具体步骤

下载 `shardingsphere-proxy` 的二进制包 -->[shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin.tar.gz](https://www.apache.org/dyn/closer.cgi/shardingsphere/5.0.0-alpha/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin.tar.gz)

> 根据提示，需要自行将数据库驱动包放入 proxy 的 lib 文件夹中

解压后修改 config 中的 `config-replica-query.yaml` ，我的配置信息如下：（采用默认账户 root/root，默认端口3307）

```
schemaName: db
dataSourceCommon:
  username: root
  password: 123456
  connectionTimeoutMilliseconds: 30000
  idleTimeoutMilliseconds: 60000
  maxLifetimeMilliseconds: 1800000
  maxPoolSize: 50
  minPoolSize: 1
  maintenanceIntervalMilliseconds: 30000
dataSources:
  primary_ds:
    url: jdbc:mysql://127.0.0.1:3316/db?serverTimezone=UTC&useSSL=false
  replica_ds_0:
    url: jdbc:mysql://127.0.0.1:3326/db?serverTimezone=UTC&useSSL=false
  replica_ds_1:
    url: jdbc:mysql://127.0.0.1:3336/db?serverTimezone=UTC&useSSL=false
rules:
- !REPLICA_QUERY
  dataSources:
    pr_ds:
      name: pr_ds
      primaryDataSourceName: primary_ds
      replicaDataSourceNames:
        - replica_ds_0
        - replica_ds_1
```

server.xml配置如下：

```
authentication:
  users:
    root:
      password: root

props:
 max-connections-size-per-query: 1
 acceptor-size: 16  # The default value is available processors count * 2.
 executor-size: 16  # Infinite by default.
 proxy-frontend-flush-threshold: 128  # The default value is 128.
 proxy-transaction-type: LOCAL
 proxy-opentracing-enabled: false
 proxy-hint-enabled: false
 query-with-cipher-column: false
 sql-show: true
 check-table-metadata-enabled: false
```

