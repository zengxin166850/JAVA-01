package com.zengxin.homework08.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    private Integer orderId;

    private Integer userId;

    private Integer status;

}
