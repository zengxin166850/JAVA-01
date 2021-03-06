package com.zengxin.homework0703.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    private Integer id;

    private Integer user_id;

    private Integer product_id;

    private Double price;

    private Date create_time;

    private Date pay_time;

}
