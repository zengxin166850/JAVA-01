package com.zengxin.tccdemo.common.mapper;

import com.zengxin.tccdemo.common.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * OrderMapper
 */
public interface OrderMapper {

    /**
     * 保存订单.
     */
    @Insert(" insert into `order` (create_time,number,status,product_id,total_amount,count,user_id) " +
            " values ( #{createTime},#{number},#{status},#{productId},#{totalAmount},#{count},#{userId})")
    int save(Order order);

    /**
     * 更新订单.
     */
    @Update("update `order` set status = #{status}  where number = #{number}")
    int update(Order order);

    /**
     * 获取所有的订单
     */
    @Select("select * from  order")
    List<Order> listAll();
}
