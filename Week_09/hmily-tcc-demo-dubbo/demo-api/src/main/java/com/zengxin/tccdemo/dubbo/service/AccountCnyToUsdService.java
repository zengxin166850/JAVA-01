package com.zengxin.tccdemo.dubbo.service;

import com.zengxin.tccdemo.dubbo.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

public interface AccountCnyToUsdService {
    @Hmily
    int cnyToUsd(AccountDTO accountDTO);
}
