package com.zengxin.tccdemo.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户
 */
@Data
public class Account implements Serializable {

    private static final long serialVersionUID = -81849676368907419L;

    private Integer id;
    private String userId;

    /**
     * 用户余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal freezeAmount;
    private Date createTime;
    private Date updateTime;
}
