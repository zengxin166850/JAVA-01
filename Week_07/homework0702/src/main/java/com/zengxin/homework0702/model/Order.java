package com.zengxin.homework0702.model;

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

    private int id;

    private int user_id;

    private int product_id;

    private double price;

    private Date create_time;

    private Date pay_time;

}
