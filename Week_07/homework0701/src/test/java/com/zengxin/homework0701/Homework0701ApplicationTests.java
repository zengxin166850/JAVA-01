package com.zengxin.homework0701;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
class Homework0701ApplicationTests {

    private static final String INSERT_SQL = "insert into t_order(user_id,product_id,price,create_time,pay_time) values(?,?,?,?,?)";

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws InterruptedException {

    }

    @Test
    public void batchInsert() {
        long start = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<?>> futureTasks = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Future<?> submit = threadPool.submit(() -> {
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);) {
                    for (int i = 1; i <= 100000; i++) {
                        preparedStatement.setInt(1, i);
                        preparedStatement.setInt(2, i);
                        preparedStatement.setDouble(3, i);
                        Date date = new Date(System.currentTimeMillis());
                        preparedStatement.setDate(4, date);
                        preparedStatement.setDate(5, date);
                        preparedStatement.addBatch();
                        if (i % 10000 == 0) {
                            preparedStatement.executeBatch();
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            futureTasks.add(submit);
        }
        futureTasks.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        long time1 = System.currentTimeMillis();
        System.out.println("使用线程池批量增加的耗时为：" + (time1 - start));
    }

}