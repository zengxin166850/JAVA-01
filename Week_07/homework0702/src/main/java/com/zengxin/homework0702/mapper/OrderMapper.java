package com.zengxin.homework0702.mapper;

import com.zengxin.homework0702.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 写
     *
     * @param order 订单
     * @return 数量
     */
    @Insert("insert into t_order values(#{id},#{user_id},#{product_id},#{price},#{create_time},#{pay_time})")
    int insert(Order order);

    /**
     * 读
     *
     * @param id 主键
     * @return Order
     */
    @Select("select * from t_order where id = #{id}")
    Order selectOne(int id);
}
