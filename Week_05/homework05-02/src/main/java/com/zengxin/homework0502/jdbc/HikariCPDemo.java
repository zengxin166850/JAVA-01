package com.zengxin.homework0502.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class HikariCPDemo {

    public static void main(String[] args) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimezone=UTC");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("123456");
        Connection connection = null;
        try (HikariDataSource dataSource = new HikariDataSource(hikariConfig)) {
            connection = dataSource.getConnection();
            //关闭自动提交
            connection.setAutoCommit(false);
            //insert
            PreparedStatement pst = connection.prepareStatement("insert into student values (?,?)");
            for(int i=0;i<100;i++){
                pst.setInt(1,i);
                pst.setString(2,"name"+i);
                pst.addBatch();
            }
            pst.executeBatch();
            pst.close();
            connection.commit();
            //delete
            PreparedStatement pst2 = connection.prepareStatement("delete from student where id > 90");
            pst2.executeUpdate();
            pst2.close();
            connection.commit();
            //update
            PreparedStatement pst3 = connection.prepareStatement("update student set name=? where id = ?");
            for(int i=0;i<10;i++){
                pst3.setString(1,i+"name");
                pst3.setInt(2,i);
                pst3.addBatch();
            }
            pst3.executeBatch();
            pst3.close();
            connection.commit();
            //select
            PreparedStatement pst4 = connection.prepareStatement("select * from student");
            ResultSet rs = pst4.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("id")+": "+ rs.getString("name"));
            }
        }finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
