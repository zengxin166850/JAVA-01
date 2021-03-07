package com.zengxin.homework0704;

import com.zengxin.homework0704.model.Order;
import com.zengxin.homework0704.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Homework0704ApplicationTests {

//	@Test
//	void contextLoads() {
//	}

    @Autowired
    OrderService orderService;

    @Test
    public void TestWrite1() {
        Order order3 = new Order(3, 3, 3, 3.0d, new Date(), new Date());
        int insert = orderService.insert(order3);

        Order order4 = new Order(4, 4, 4, 4.0d, new Date(), new Date());
        orderService.insert(order4);
        System.out.println("新增了两条数据到master...");
    }

    @Test
    public void TestRead1() {
        Order order1 = orderService.selectOne(3);
        System.out.println(order1.toString());
        System.out.println("第一次查询...");

        Order order2 = orderService.selectOne(4);
        System.out.println(order2.toString());
        System.out.println("第二次查询...");

    }
}
