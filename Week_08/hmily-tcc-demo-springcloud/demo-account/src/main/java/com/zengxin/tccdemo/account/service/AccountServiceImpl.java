package com.zengxin.tccdemo.account.service;

import com.zengxin.tccdemo.account.client.InventoryClient;
import com.zengxin.tccdemo.common.dto.AccountDTO;
import com.zengxin.tccdemo.common.dto.AccountNestedDTO;
import com.zengxin.tccdemo.common.dto.InventoryDTO;
import com.zengxin.tccdemo.common.mapper.AccountMapper;
import com.zengxin.tccdemo.common.model.Account;
import com.zengxin.tccdemo.common.service.AccountService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountMapper accountMapper;

    private final InventoryClient inventoryClient;

    @Autowired(required = false)
    public AccountServiceImpl(final AccountMapper accountMapper, final InventoryClient inventoryClient) {
        this.accountMapper = accountMapper;
        this.inventoryClient = inventoryClient;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean payment(AccountDTO accountDTO) {
        LOGGER.info("============执行try付款接口===============");
        accountMapper.update(accountDTO);
        return Boolean.TRUE;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean mockTryPaymentException(AccountDTO accountDTO) {
        throw new HmilyRuntimeException("账户扣减异常！");
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean mockTryPaymentTimeout(AccountDTO accountDTO) {
        try {
            //模拟延迟 当前线程暂停10秒
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int decrease = accountMapper.update(accountDTO);
        if (decrease != 1) {
            throw new HmilyRuntimeException("账户余额不足");
        }
        return true;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean paymentWithNested(AccountNestedDTO accountNestedDTO) {
        accountMapper.update(buildAccountDTO(accountNestedDTO));
        inventoryClient.decrease(buildInventoryDTO(accountNestedDTO));
        return Boolean.TRUE;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean paymentWithNestedException(AccountNestedDTO accountNestedDTO) {
        accountMapper.update(buildAccountDTO(accountNestedDTO));
        inventoryClient.mockWithTryException(buildInventoryDTO(accountNestedDTO));
        return Boolean.TRUE;
    }

    @Override
    public Account findByUserId(String userId) {
        return accountMapper.findByUserId(userId);
    }

    /**
     * confirm
     */
    public boolean confirm(final AccountDTO accountDTO) {
        LOGGER.info("============执行confirm 付款接口===============");
        return accountMapper.confirm(accountDTO) > 0;
    }

    /**
     * cancel
     */
    public boolean cancel(final AccountDTO accountDTO) {
        LOGGER.info("============执行cancel 付款接口===============");
        return accountMapper.cancel(accountDTO) > 0;
    }

    private AccountDTO buildAccountDTO(AccountNestedDTO nestedDTO) {
        AccountDTO dto = new AccountDTO();
        dto.setAmount(nestedDTO.getAmount());
        dto.setUserId(nestedDTO.getUserId());
        return dto;
    }

    private InventoryDTO buildInventoryDTO(AccountNestedDTO nestedDTO) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(nestedDTO.getCount());
        inventoryDTO.setProductId(nestedDTO.getProductId());
        return inventoryDTO;
    }
}
