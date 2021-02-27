### 第 12 节课作业实践
#### 2、（必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交DDL 的 SQL 文件到 Github（后面2周的作业依然要是用到这个表结构）。

```
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL COMMENT '主键',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别（1男，2女）',
  `phone` char(11) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```
CREATE TABLE `t_product` (
  `id` int(11) NOT NULL COMMENT '主键',
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10,0) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

```
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



#### 5、（选做）：尝试对 MySQL 不同引擎下测试100万订单数据的增删改查性能。

在使用上述 `innodb` 引擎下运行如下 `sql` 语句，创建存储过程 `idata`，用于导入`100w` 订单数据：

```
# myisam 引擎
create table t_order_myisam like t_order;
alter table t_order_myisam engine=myisam;

# memory 引擎
create table t_order_memory like t_order;
alter table t_order_memory engine=memory;
#存储过程
drop procedure IF EXISTS idata;
delimiter ;;
create procedure idata()
begin
  declare i int;
  set i=1;
  while(i<=1000000)do
    insert into t_order values(i, i, i,RAND()*i,NOW(),NOW());
    set i=i+1;
  end while;
end;;
delimiter ;
# 初次调用idata 后，分别修改 inert 表名为t_order_myisam、t_order_memory再次调用，
call idata();

```

插入速度测试结果：

```
# innodb测试时分别调整参数为0和1测试一次
SET GLOBAL sync_binlog=0 ;
SET GLOBAL innodb_flush_log_at_trx_commit = 0;
```

| 引擎名 | 使用双1配置（执行时长）      | 使用双0配置（执行时长） |
| ------ | ---------------------------- | ----------------------- |
| innodb | 637.473s                     | 64.374s                 |
| myisam | 60.995s                      | 61.501s                 |
| memory | 失败 tmp_table_size  is full | ...                     |

查询、删除速度对比：

```
[SQL]SELECT * FROM t_order;
受影响的行: 0
时间: 0.930s

[SQL]SELECT * FROM t_order_myisam;
受影响的行: 0
时间: 0.650s

[SQL]DELETE  FROM t_order;
受影响的行: 1000000
时间: 2.656s

[SQL]DELETE  FROM t_order_myisam;
受影响的行: 1000000
时间: 0.005s
```

**结论：**

增删改查速度上，myisam 都要优于 innodb。在不需要事务的场景（例如历史数据、日志类数据，只提供查询，不需要事务修改）可以考虑使用 myisam 引擎

```
# 数据写入结束、改回双1配置
SET GLOBAL sync_binlog=1;
SET GLOBAL innodb_flush_log_at_trx_commit = 1;
```

#### 6、（选做）：模拟1000万订单数据，测试不同方式下导入导出（数据备份还原）MySQL 的速度，包括 jdbc 程序处理和命令行处理。思考和实践，如何提升处理效率。

修改idata循环次数为1000万，在清空t_order后重新执行：

**1.使用mysqldump：**(导出约10s左右、还原时间长)

```
# 一条insert包含values多条数据
 .\mysqldump.exe -h 127.0.0.1 -P 3306 -u root -p  --add-locks=0 --no-create-info --single-transaction --set-gtid-purged=OFF test t_order  --result-file=E:/t_order.sql

#如果希望每条记录对应一条 insert 语句，可以加上 –skip-extended-insert 参数
```

2.导出csv  + load data指令（导出约10s左右、还原时间长）

```

select * from t_order into outfile 'C:\ProgramData\MySQL\MySQL Server 5.7\Uploads\t_order.csv';

load data infile 'C:\ProgramData\MySQL\MySQL Server 5.7\Uploads\t_order.csv' into table db2.t;
```

3.程序查数据库，拼接sql后写入文件。（导出时间长、还原时间长）

**加速处理方式：**

1. 导出其实很快（想要更快可以考虑多线程，`mysqldump` 是单线程的）
2. 导入时可以考虑关闭` binlog` 和 `redolog` 的双 `1` 配置，暂时调整为 `0` ，完成后改回双一(第5题的实验结果可以看出，写入速度提升很明显)

#### 7、（选做）：对 MySQL 配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查100万次，对比性能，生成报告。
Hikari > Druid  > DBCP > C3p0