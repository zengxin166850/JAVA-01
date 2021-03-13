package com.zengxin.tccdemo.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存
 */
@Data
public class Inventory implements Serializable {

    private static final long serialVersionUID = 6957734749389133832L;

    private Integer id;
    private String productId;

    /**
     * 总库存
     */
    private Integer totalInventory;

    /**
     * 锁定库存
     */
    private Integer lockInventory;
}
