

package com.zengxin.tccdemo.common.service;

import com.zengxin.tccdemo.common.dto.AccountDTO;
import com.zengxin.tccdemo.common.dto.AccountNestedDTO;
import com.zengxin.tccdemo.common.model.Account;
import org.dromara.hmily.annotation.Hmily;

/**
 * The interface Account service.
 *
 * @author xiaoyu
 */
public interface AccountService {
    
    /**
     * 扣款支付
     *
     * @param accountDTO 参数dto
     */
    @Hmily
    boolean payment(AccountDTO accountDTO);
    
    /**
     * Mock try payment exception.
     *
     * @param accountDTO the account dto
     */
    @Hmily
    boolean mockTryPaymentException(AccountDTO accountDTO);
    
    /**
     * Mock try payment timeout.
     *
     * @param accountDTO the account dto
     */
    @Hmily
    boolean mockTryPaymentTimeout(AccountDTO accountDTO);
    
    /**
     * 扣款支付
     *
     * @param accountNestedDTO 参数dto
     * @return true boolean
     */
    @Hmily
    boolean paymentWithNested(AccountNestedDTO accountNestedDTO);
    
    /**
     * Payment with nested exception boolean.
     *
     * @param accountNestedDTO the account nested dto
     * @return the boolean
     */
    @Hmily
    boolean paymentWithNestedException(AccountNestedDTO accountNestedDTO);
    
    /**
     * 获取用户账户信息
     *
     * @param userId 用户id
     * @return AccountDO account do
     */
    Account findByUserId(String userId);
}
