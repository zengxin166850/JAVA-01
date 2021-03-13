package com.zengxin.tccdemo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     *
     */
    NOT_PAY(1, "未支付"),

    /**
     *
     */
    PAYING(2, "支付中"),

    /**
     *
     */
    PAY_FAIL(3, "支付失败"),

    /**
     *
     */
    PAY_SUCCESS(4, "支付成功");

    private final int code;

    private final String desc;
}