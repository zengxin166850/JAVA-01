package com.zengxin.tccdemo.common.mapper;

import com.zengxin.tccdemo.common.dto.AccountDTO;
import com.zengxin.tccdemo.common.model.Account;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * AccountMapper
 */
public interface AccountMapper {

    /**
     * 更新
     */
    @Update("update account set balance = balance - #{amount},freeze_amount= freeze_amount + #{amount}," +
            "update_time = now() where user_id =#{userId}  and  balance > 0  ")
    int update(AccountDTO accountDTO);


    /**
     * Confirm 确认
     */
    @Update("update account set freeze_amount= freeze_amount - #{amount}" +
            " where user_id =#{userId}  and freeze_amount >0 ")
    int confirm(AccountDTO accountDTO);

    /**
     * Cancel 取消
     */
    @Update("update account set balance = balance + #{amount},freeze_amount= freeze_amount -  #{amount} " +
            " where user_id =#{userId}  and freeze_amount >0")
    int cancel(AccountDTO accountDTO);

    /**
     * 根据userId获取用户账户信息
     */
    @Select("select id,user_id,balance, freeze_amount from account where user_id =#{userId} limit 1")
    Account findByUserId(String userId);
}
