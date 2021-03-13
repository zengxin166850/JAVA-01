package com.zengxin.tccdemo.order.service;


import com.zengxin.tccdemo.common.model.Order;

import java.math.BigDecimal;


public interface OrderService {
    
    /**
     * 事务成功
     */
    String orderPay(Integer count, BigDecimal amount);
    
    /**
     * try 库存异常
     */
    String mockInventoryWithTryException(Integer count, BigDecimal amount);
    
    /**
     * try 账户异常
     */
    String mockAccountWithTryException(Integer count, BigDecimal amount);
    
    /**
     * try 库存超时
     */
    String mockInventoryWithTryTimeout(Integer count, BigDecimal amount);
    
    /**
     * try账户超时
     */
    String mockAccountWithTryTimeout(Integer count, BigDecimal amount);
    
    /**
     * 嵌套调用
     */
    String orderPayWithNested(Integer count, BigDecimal amount);
    
    /**
     * 嵌套调用异常
     */
    String orderPayWithNestedException(Integer count, BigDecimal amount);
    
    /**
     * 更新订单状态.
     *
     * @param order 订单实体类
     */
    void updateOrderStatus(Order order);
}
