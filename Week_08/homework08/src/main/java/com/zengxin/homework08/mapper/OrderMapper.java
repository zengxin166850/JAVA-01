package com.zengxin.homework08.mapper;

import com.zengxin.homework08.model.Order;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {

    /**
     * 写
     *
     * @param order 订单
     * @return 数量
     */
    @Insert("insert into t_order(order_id,user_id,status) values(#{orderId},#{userId},#{status})")
    int insert(Order order);

    /**
     * 读
     *
     * @return Order
     */
    @Select("select order_id orderId,user_id userId,status from t_order where order_id = #{orderId}")
    Order selectOne(int orderId);

    @Update("update t_order set status = 1 where order_id = #{orderId}")
    int update(int orderId);

    @Delete("delete from t_order where order_id = #{orderId}")
    int deleteByOrderId(int orderId);

    @Delete("delete from t_order where user_id = #{userId}")
    int deleteByUserId(int userId);
}
