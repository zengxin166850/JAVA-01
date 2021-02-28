package com.zengxin.homework0701;

import java.sql.*;

public class BatchInsertDemo {
    private static final String INSERT_SQL = "insert into t_order values(?,?,?,?,?,?)";

    public static void main(String[] args) throws SQLException {
        /**
         * 未使用 rewriteBatchedStatements 参数时，关闭binlog和redolog双一，可达到80s左右
         * 开启 rewriteBatchedStatements 后双1模式下也仅需 8.1s
         * 100w条耗时约 8149 ms
         */
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&&rewriteBatchedStatements=true";
        long start = System.currentTimeMillis();
        try (Connection connection = DriverManager.getConnection(url, "root", "123456");
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);) {
            for (int i = 1; i <= 1000000; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setInt(2, i);
                preparedStatement.setInt(3, i);
                preparedStatement.setDouble(4, i);
                Date date = new Date(System.currentTimeMillis());
                preparedStatement.setDate(5, date);
                preparedStatement.setDate(6, date);
                preparedStatement.addBatch();
                if (i % 10000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            long time1 = System.currentTimeMillis();
            System.out.println("使用preparedStatement批量插入100w条记录共耗时：" + (time1 - start) + " ms");
        }

    }
}
