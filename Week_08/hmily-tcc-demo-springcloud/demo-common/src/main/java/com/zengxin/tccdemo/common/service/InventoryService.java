

package com.zengxin.tccdemo.common.service;

import com.zengxin.tccdemo.common.dto.InventoryDTO;
import com.zengxin.tccdemo.common.model.Inventory;
import org.dromara.hmily.annotation.Hmily;

public interface InventoryService {
    
    /**
     * 扣减库存操作
     * 这一个tcc接口
     *
     * @param inventoryDTO 库存DTO对象
     * @return true boolean
     */
    @Hmily
    Boolean decrease(InventoryDTO inventoryDTO);
    

    /**
     * 获取商品库存信息
     *
     * @param productId 商品id
     * @return InventoryDO inventory do
     */
    Inventory findByProductId(String productId);
    
    /**
     * mock扣减库存异常
     *
     * @param inventoryDTO dto对象
     * @return String string
     */
    @Hmily
    String mockWithTryException(InventoryDTO inventoryDTO);
    
    /**
     * mock扣减库存超时
     *
     * @param inventoryDTO dto对象
     * @return String boolean
     */
    @Hmily
    Boolean mockWithTryTimeout(InventoryDTO inventoryDTO);
    
    /**
     * mock 扣减库存confirm超时
     *
     * @param inventoryDTO dto对象
     * @return True boolean
     */
    @Hmily
    Boolean mockWithConfirmTimeout(InventoryDTO inventoryDTO);
}
