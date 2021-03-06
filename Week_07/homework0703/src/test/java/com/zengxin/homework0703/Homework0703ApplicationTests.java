package com.zengxin.homework0703;

import com.zengxin.homework0703.model.Order;
import com.zengxin.homework0703.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Homework0703ApplicationTests {

    @Autowired
    OrderService orderService;

    @Test
    public void TestWrite1() {
        Order order = new Order(2, 2, 2, 2.0d, new Date(), new Date());
        int insert = orderService.insert(order);
        System.out.println("影响条数：" + insert);
    }

    @Test
    public void TestRead1() {
        Order order1 = orderService.selectOne(2);
        System.out.println(order1.toString());

        Order order2 = orderService.selectOne(2);
        System.out.println(order2.toString());
    }

}
