package com.zengxin.tccdemo.common.mapper;

import com.zengxin.tccdemo.common.dto.InventoryDTO;
import com.zengxin.tccdemo.common.model.Inventory;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * InventoryMapper
 */
public interface InventoryMapper {

    /**
     * 扣库存、冻结
     */
    @Update("update inventory set total_inventory = total_inventory - #{count}," +
            " lock_inventory= lock_inventory + #{count} " +
            " where product_id =#{productId} and total_inventory > 0  ")
    int decrease(InventoryDTO inventoryDTO);


    /**
     * 确认 Confirm
     */
    @Update("update inventory set lock_inventory = lock_inventory - #{count} " +
            " where product_id =#{productId} and lock_inventory > 0 ")
    int confirm(InventoryDTO inventoryDTO);

    /**
     * 取消 Cancel
     */
    @Update("update inventory set total_inventory = total_inventory + #{count}," +
            " lock_inventory= lock_inventory - #{count} where product_id =#{productId}  and lock_inventory > 0 ")
    int cancel(InventoryDTO inventoryDTO);

    /**
     * 通过商品id查询库存
     */
    @Select("select id,product_id,total_inventory ,lock_inventory from inventory where product_id =#{productId}")
    Inventory findByProductId(String productId);
}
