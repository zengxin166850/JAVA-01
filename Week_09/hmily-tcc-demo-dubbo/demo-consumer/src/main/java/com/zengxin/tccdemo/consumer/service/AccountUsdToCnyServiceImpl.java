package com.zengxin.tccdemo.consumer.service;

import com.zengxin.tccdemo.dubbo.dto.AccountDTO;
import com.zengxin.tccdemo.dubbo.mapper.AccountMapper;
import com.zengxin.tccdemo.dubbo.service.AccountUsdToCnyService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service("accountUsdToCnyService")
public class AccountUsdToCnyServiceImpl implements AccountUsdToCnyService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmUSDChange", cancelMethod = "cancelUSDChange")
    public int usdToCny(AccountDTO accountDTO) {
        //1美元-->7人民币
        accountDTO.setUserId("2");
        accountDTO.setAmount(new BigDecimal(1));
        return accountMapper.usdToCny(accountDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int confirmUSDChange(AccountDTO accountDTO) {
        return accountMapper.confirmUSDChange(accountDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public int cancelUSDChange(AccountDTO accountDTO) {
        return accountMapper.cancelUSDChange(accountDTO);
    }

}