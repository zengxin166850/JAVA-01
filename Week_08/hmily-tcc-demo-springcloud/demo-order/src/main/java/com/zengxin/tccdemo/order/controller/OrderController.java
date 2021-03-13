package com.zengxin.tccdemo.order.controller;

import com.zengxin.tccdemo.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/orderPay")
    @ApiOperation(value = "事务成功")
    public String orderPay(@RequestParam(value = "count") Integer count,
                           @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.orderPay(count, amount);
    }

    @PostMapping(value = "/mockInventoryWithTryException")
    @ApiOperation(value = "try阶段异常")
    public String mockInventoryWithTryException(@RequestParam(value = "count") Integer count,
                                                @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.mockInventoryWithTryException(count, amount);
    }

    @PostMapping(value = "/mockInventoryWithTryTimeout")
    @ApiOperation(value = "try阶段超时异常（但是最后自身成功了）")
    public String mockInventoryWithTryTimeout(@RequestParam(value = "count") Integer count,
                                              @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.mockInventoryWithTryTimeout(count, amount);
    }
    
    @PostMapping(value = "/mockAccountWithTryException")
    @ApiOperation(value = "try阶段，账户rpc异常")
    public String mockAccountWithTryException(@RequestParam(value = "count") Integer count,
                                              @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.mockAccountWithTryException(count, amount);
    }
    
    @PostMapping(value = "/mockAccountWithTryTimeout")
    @ApiOperation(value = "try阶段时候，账户rpc超时异常（但是最后自身成功了）")
    public String mockAccountWithTryTimeout(@RequestParam(value = "count") Integer count,
                                            @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.mockAccountWithTryTimeout(count, amount);
    }
    
    @PostMapping(value = "/orderPayWithNested")
    @ApiOperation(value = "rpc的嵌套调用 A--> B--> C 成功")
    public String orderPayWithNested(@RequestParam(value = "count") Integer count,
                                     @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.orderPayWithNested(count, amount);
    }
    
    @PostMapping(value = "/orderPayWithNestedException")
    @ApiOperation(value = "rpc的嵌套调用A-->B-->C, C出现异常")
    public String orderPayWithNestedException(@RequestParam(value = "count") Integer count,
                                              @RequestParam(value = "amount") BigDecimal amount) {
        return orderService.orderPayWithNestedException(count, amount);
    }
}
