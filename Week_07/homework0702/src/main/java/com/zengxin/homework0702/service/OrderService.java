package com.zengxin.homework0702.service;

import com.zengxin.homework0702.aop.ReadOnly;
import com.zengxin.homework0702.aop.WriteOnly;
import com.zengxin.homework0702.mapper.OrderMapper;
import com.zengxin.homework0702.model.Order;
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
    @WriteOnly
    public int insert(Order order) {
        return orderMapper.insert(order);
    }

    /**
     * selectOne
     *
     * @param id id
     * @return Order
     */
    @ReadOnly
    public Order selectOne(int id) {
        return orderMapper.selectOne(id);
    }
}
