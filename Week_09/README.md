## 第 17  课作业实践

#### 3、（必做）改造自定义RPC的程序，提交到github：
- [x] ##### 1）尝试将服务端写死查找接口实现类变成泛型和反射

- [x] ##### 2）尝试将客户端动态代理改成AOP，添加异常处理

- [x] ##### 3）尝试使用Netty+HTTP作为client端传输方式

#### 4、（选做☆☆）升级自定义RPC的程序：
- [ ] ##### 1）尝试使用压测并分析优化RPC性能

- [ ] ##### 2）尝试使用Netty+TCP作为两端传输方式

- [ ] ##### 3）尝试自定义二进制序列化

- [ ] ##### 4）尝试压测改进后的RPC并分析优化，有问题欢迎群里讨论

- [ ] ##### 5）尝试将fastjson改成xstream

- [ ] ##### 6）尝试使用字节码生成方式代替服务端反射

- [x] ##### 7）基于zookeeper的注册中心，消费者和生产者可以根据注册中心查找可用服务进行调用(直接选择列表里的最后一个)。

- [x] ##### 8）当有生产者启动或者下线时，通过zookeeper通知并更新各个消费者，使得各个消费者可以调用新生产者或者不调用下线生产者。

程序见  [rpcfx](./rpcfx)

## 第  18 课作业实践

#### 3、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。

表结构设计如下：

```
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `cny_balance` decimal(10,0) NOT NULL COMMENT '人民币用户余额',
  `usd_balance` decimal(10,0) NOT NULL COMMENT '美元用户余额',
  `cny_freeze_amount` decimal(10,0) NOT NULL COMMENT '人民币冻结金额，扣款暂存余额',
  `usd_freeze_amount` decimal(10,0) NOT NULL COMMENT '美元冻结金额，扣款暂存余额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
-- A库
INSERT INTO `hmily_account`.`account` (`id`, `user_id`, `cny_balance`,`usd_balance`, 
`cny_freeze_amount`,`usd_freeze_amount`, `create_time`, `update_time`) 
VALUES ('1', '1', '10000000', '0','0','0', '2017-09-18 14:54:22', '2021-03-14 01:18:37');

-- B库
INSERT INTO `hmily_account`.`account` (`id`, `user_id`, `cny_balance`,`usd_balance`, 
`cny_freeze_amount`,`usd_freeze_amount`, `create_time`, `update_time`) 
VALUES ('2', '2', '0', '10000000','0','0', '2017-09-18 14:54:22', '2021-03-14 01:18:37');
```



两个账户在不同数据库中，所以分别设计了人民币-->美元, 美元-->人民币两个服务。大致如下

```
public interface AccountCnyToUsdService {
    @Hmily
    int cnyToUsd(AccountDTO accountDTO);
}

public interface AccountUsdToCnyService {

    @Hmily
    int usdToCny(AccountDTO accountDTO);
}
```

在服务端 spring-dubbo.xml中配置好服务，如下

```
<dubbo:service interface="org.dromara.hmily.demo.common.inventory.api.InventoryService"
                   ref="inventoryService" executes="20"/>
```

消费端通过`<dubbo:reference>`引入相应的代理桩

```
<dubbo:reference timeout="500000000"
                     interface="com.zengxin.tccdemo.dubbo.service.AccountCnyToUsdService"
                     id="accountCnyToUsdService"
                     retries="0" check="false" actives="20" loadbalance="hmilyRandom"/>
```

详细内容见 [hmily-tcc-demo-dubbo](./hmily-tcc-demo-dubbo)



#### 问题记录：

- `hmily metric prometheus`端口冲突

- 包名错误，注入失败

