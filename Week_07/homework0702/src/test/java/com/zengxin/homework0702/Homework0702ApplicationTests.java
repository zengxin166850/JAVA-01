package com.zengxin.homework0702;

import com.zengxin.homework0702.model.Order;
import com.zengxin.homework0702.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class Homework0702ApplicationTests {

	@Autowired
	OrderService orderService;

	@Test
	public void TestWrite1(){
		Order order = new Order(1,1,1,1.0d,new Date(),new Date());
		int insert = orderService.insert(order);
		System.out.println("影响条数："+insert);
	}

	@Test
	public void TestRead1(){
		Order order = orderService.selectOne(1);
		System.out.println(order.toString());
	}

}
