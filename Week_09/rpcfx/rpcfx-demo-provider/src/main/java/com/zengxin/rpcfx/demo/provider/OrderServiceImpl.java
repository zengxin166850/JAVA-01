package com.zengxin.rpcfx.demo.provider;

import com.zengxin.rpcfx.annotation.RPCService;
import com.zengxin.rpcfx.demo.api.Order;
import com.zengxin.rpcfx.demo.api.OrderService;

@RPCService
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "order" + System.currentTimeMillis(), 9.9f);
    }
}
