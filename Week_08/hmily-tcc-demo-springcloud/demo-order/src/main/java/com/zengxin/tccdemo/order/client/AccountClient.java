package com.zengxin.tccdemo.order.client;

import com.zengxin.tccdemo.common.dto.AccountDTO;
import com.zengxin.tccdemo.common.dto.AccountNestedDTO;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * rpc调用账户服务
 */
@FeignClient(value = "account-service")
public interface AccountClient {

    /**
     * 用户账户付款.
     */
    @RequestMapping("/account/payment")
    @Hmily
    Boolean payment(@RequestBody AccountDTO accountDO);

    /**
     * 获取用户账户信息.
     */
    @RequestMapping("/account/findByUserId")
    BigDecimal findByUserId(@RequestParam("userId") String userId);

    /**
     * 模拟try阶段异常
     */
    @Hmily
    @RequestMapping("/account/mockWithTryException")
    Boolean mockWithTryException(@RequestBody AccountDTO accountDO);

    /**
     * 模拟try超时
     */
    @Hmily
    @RequestMapping("/account/mockWithTryTimeout")
    Boolean mockWithTryTimeout(@RequestBody AccountDTO accountDO);

    /**
     * 嵌套调用
     */
    @Hmily
    @RequestMapping("/account/paymentWithNested")
    Boolean paymentWithNested(@RequestBody AccountNestedDTO nestedDTO);

    /**
     * 嵌套调用异常
     */
    @Hmily
    @RequestMapping("/account/paymentWithNestedException")
    Boolean paymentWithNestedException(@RequestBody AccountNestedDTO nestedDTO);
}
