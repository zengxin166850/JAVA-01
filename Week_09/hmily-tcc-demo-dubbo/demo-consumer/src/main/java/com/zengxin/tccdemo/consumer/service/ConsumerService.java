package com.zengxin.tccdemo.consumer.service;

import com.zengxin.tccdemo.dubbo.dto.AccountDTO;
import com.zengxin.tccdemo.dubbo.service.AccountCnyToUsdService;
import com.zengxin.tccdemo.dubbo.service.AccountUsdToCnyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConsumerService {

    @Autowired
    AccountUsdToCnyService usdToCny;

    @Autowired
    AccountCnyToUsdService cnyToUsd;

    /**
     * 只是调用了两个事务，本身不需要tcc
     */
    public boolean exchange() {
        usdToCny.usdToCny(new AccountDTO());
        //7人民币-->1美元
        AccountDTO userA = new AccountDTO();
        userA.setUserId("1");
        userA.setAmount(new BigDecimal(7));
        cnyToUsd.cnyToUsd(userA);
        return true;
    }

}
