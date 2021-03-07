package com.zengxin.homework0704.service;


import com.zengxin.homework0704.mapper.OrderMapper;
import com.zengxin.homework0704.model.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * service
 */
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * insert
     *
     * @param order order
     * @return int
     */
    public int insert(Order order) {
        return orderMapper.insert(order);
    }

    /**
     * selectOne
     *
     * @param id id
     * @return Order
     */
    public Order selectOne(int id) {
        return orderMapper.selectOne(id);
    }
}
