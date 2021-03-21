package com.zengxin.tccdemo.account.service;

import com.zengxin.tccdemo.dubbo.dto.AccountDTO;
import com.zengxin.tccdemo.dubbo.mapper.AccountMapper;
import com.zengxin.tccdemo.dubbo.service.AccountCnyToUsdService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("accountCnyToUsdService")
public class AccountCnyToUsdServiceImpl implements AccountCnyToUsdService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmCNYChange", cancelMethod = "cancelCNYChange")
    public int cnyToUsd(AccountDTO accountDTO) {
        return accountMapper.cnyToUsd(accountDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int confirmCNYChange(AccountDTO accountDTO) {
        return accountMapper.confirmCNYChange(accountDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int cancelCNYChange(AccountDTO accountDTO) {
        return accountMapper.cancelCNYChange(accountDTO);
    }

}