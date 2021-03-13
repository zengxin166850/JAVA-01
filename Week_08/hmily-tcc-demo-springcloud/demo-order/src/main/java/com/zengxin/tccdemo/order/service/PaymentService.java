package com.zengxin.tccdemo.order.service;


import com.zengxin.tccdemo.common.model.Order;

public interface PaymentService {

    /**
     * 订单支付.
     */
    void makePayment(Order order);

    /**
     * 模拟订单支付的时候库存异常
     */
    String mockPaymentInventoryWithTryException(Order order);

    /**
     * 模拟订单支付的时候账户异常
     */
    String mockPaymentAccountWithTryException(Order order);

    /**
     * 模拟订单支付的时候库存超时
     */
    String mockPaymentInventoryWithTryTimeout(Order order);

    /**
     * 模拟订单支付的时候账户超时
     */
    String mockPaymentAccountWithTryTimeout(Order order);

    /**
     * 模拟嵌套调用
     *
     * @param order the order
     * @return the string
     */
    String makePaymentWithNested(Order order);

    /**
     * 模拟嵌套调用出现异常
     *
     * @param order the order
     * @return the string
     */
    String makePaymentWithNestedException(Order order);
}
