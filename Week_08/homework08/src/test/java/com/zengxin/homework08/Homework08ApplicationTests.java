package com.zengxin.homework08;

import com.zengxin.homework08.mapper.OrderMapper;
import com.zengxin.homework08.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Homework08ApplicationTests {

    @Autowired
    OrderMapper mapper;

    @Test
    void insert() {
        for (int i = 1; i <= 1600; i++) {
            Order order = new Order(i, (i + 1) % 25, i % 2);
            mapper.insert(order);
        }
    }

    @Test
    void update() {
        for (int i = 1; i <= 80; i++) {
            mapper.update(i);
        }
    }

    @Test
    void select() {
        Order order1 = mapper.selectOne(100);
        Order order2 = mapper.selectOne(200);
        Order order3 = mapper.selectOne(300);
        System.out.println(order1.toString());
        System.out.println(order2.toString());
        System.out.println(order3.toString());
    }

    @Test
    void delete() {
        //userid为分库键，只会到指定库中删除8个表
        mapper.deleteByUserId(99);

        //orderid时分表键。所以会到两个库中指定表执行删除，例如这里是t_order_7
        mapper.deleteByOrderId(399);
    }

}
