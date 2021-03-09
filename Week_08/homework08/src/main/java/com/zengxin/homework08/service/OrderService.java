package com.zengxin.homework08.service;


import com.zengxin.homework08.mapper.OrderMapper;
import com.zengxin.homework08.model.Order;
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
     * @return Order
     */
    public Order selectOne(int orderId) {
        return orderMapper.selectOne(orderId);
    }

    public int update(int orderId) {
        return orderMapper.update(orderId);
    }

    public int deleteByOrderId(int orderId) {
        return orderMapper.deleteByOrderId(orderId);
    }
    public int deleteByUserId(int userId) {
        return orderMapper.deleteByUserId(userId);
    }
}
