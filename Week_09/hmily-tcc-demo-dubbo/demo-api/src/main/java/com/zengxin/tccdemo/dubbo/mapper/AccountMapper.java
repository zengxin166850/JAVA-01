package com.zengxin.tccdemo.dubbo.mapper;


import com.zengxin.tccdemo.dubbo.dto.AccountDTO;
import com.zengxin.tccdemo.dubbo.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountMapper {

    /**
     * 冻结人民币
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set cny_balance = cny_balance - #{amount}," +
            " cny_freeze_amount= cny_freeze_amount + #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  cny_balance > 0  ")
    int cnyToUsd(AccountDTO accountDTO);

    /**
     * 将冻结的人民币转为美元
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set cny_freeze_amount = cny_freeze_amount - #{amount}," +
            " usd_balance= usd_balance + #{amount}/7 ,update_time = now()" +
            " where user_id =#{userId}  and  cny_freeze_amount > 0  ")
    int confirmCNYChange(AccountDTO accountDTO);

    /**
     * 将冻结的人民币返还到账户
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set cny_balance = cny_balance + #{amount}," +
            " cny_freeze_amount= cny_freeze_amount - #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  cny_freeze_amount > 0  ")
    int cancelCNYChange(AccountDTO accountDTO);


    /**
     * 冻结美元
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set usd_balance = usd_balance - #{amount}," +
            " usd_freeze_amount= usd_freeze_amount + #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  usd_balance > 0  ")
    int usdToCny(AccountDTO accountDTO);

    /**
     * 将冻结的美元转为人民币
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set usd_freeze_amount = usd_freeze_amount - #{amount}," +
            " cny_balance= cny_balance + #{amount}*7 ,update_time = now()" +
            " where user_id =#{userId}  and  usd_freeze_amount > 0  ")
    int confirmUSDChange(AccountDTO accountDTO);

    /**
     * 将冻结的美元返还到账户
     *
     * @param accountDTO accountDTO
     */
    @Update("update account set usd_balance = usd_balance + #{amount}," +
            " usd_freeze_amount= usd_freeze_amount - #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  usd_freeze_amount > 0  ")
    int cancelUSDChange(AccountDTO accountDTO);

    /**
     * 结果查询
     *
     * @param account account
     * @return account
     */
    @Select("select * from account where id = #{id}")
    Account queryOne(Account account);
}