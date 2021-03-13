package com.zengxin.tccdemo.account.controller;

import com.zengxin.tccdemo.common.dto.AccountDTO;
import com.zengxin.tccdemo.common.dto.AccountNestedDTO;
import com.zengxin.tccdemo.common.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * AccountController
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 支付
     */
    @RequestMapping("/payment")
    public Boolean payment(@RequestBody AccountDTO accountDO) {
        return accountService.payment(accountDO);
    }

    /**
     * 模拟try过程异常
     */
    @RequestMapping("/mockWithTryException")
    public Boolean mockWithTryException(@RequestBody AccountDTO accountDO) {
        return accountService.mockTryPaymentException(accountDO);
    }

    /**
     * 模拟try过程超时
     */
    @RequestMapping("/mockWithTryTimeout")
    public Boolean mockWithTryTimeout(@RequestBody AccountDTO accountDO) {
        return accountService.mockTryPaymentTimeout(accountDO);
    }

    /**
     * 模拟嵌套调用A->B->C
     */
    @RequestMapping("/paymentWithNested")
    public Boolean paymentWithNested(@RequestBody AccountNestedDTO nestedDTO) {
        return accountService.paymentWithNested(nestedDTO);
    }

    /**
     * 模拟嵌套调用异常
     */
    @RequestMapping("/paymentWithNestedException")
    public Boolean paymentWithNestedException(@RequestBody AccountNestedDTO nestedDTO) {
        return accountService.paymentWithNestedException(nestedDTO);
    }

    /**
     * 查询单个id
     */
    @RequestMapping("/findByUserId")
    public BigDecimal findByUserId(@RequestParam("userId") String userId) {
        return accountService.findByUserId(userId).getBalance();
    }
}
