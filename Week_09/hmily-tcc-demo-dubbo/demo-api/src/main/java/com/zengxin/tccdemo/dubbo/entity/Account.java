package com.zengxin.tccdemo.dubbo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Account implements Serializable {
    private static final long serialVersionUID = -81849676368907419L;
    private Integer id;

    private String userId;
    /**
     * cny余额
     */
    private BigDecimal cnyBalance;
    /**
     * usd
     */
    private BigDecimal usdBalance;
    /**
     * cny冻结金额
     */
    private BigDecimal cnyFreezeAmount;
    /**
     * usd冻结金额
     */
    private BigDecimal usdFreezeAmount;

    private Date createTime;

    private Date updateTime;
}
