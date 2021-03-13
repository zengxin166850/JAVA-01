package com.zengxin.tccdemo.order.client;

import com.zengxin.tccdemo.common.dto.InventoryDTO;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * rpc调用库存服务
 */
@FeignClient(value = "inventory-service")
public interface InventoryClient {

    /**
     * 库存扣减
     */
    @RequestMapping("/inventory/decrease")
    @Hmily
    Boolean decrease(@RequestBody InventoryDTO inventoryDTO);


    /**
     * 获取商品库存
     */
    @RequestMapping("/inventory/findByProductId")
    Integer findByProductId(@RequestParam("productId") String productId);

    /**
     * 模拟库存扣减异常
     */
    @Hmily
    @RequestMapping("/inventory/mockWithTryException")
    Boolean mockWithTryException(@RequestBody InventoryDTO inventoryDTO);

    /**
     * 模拟库存扣减超时
     */
    @Hmily
    @RequestMapping("/inventory/mockWithTryTimeout")
    Boolean mockWithTryTimeout(@RequestBody InventoryDTO inventoryDTO);
}
