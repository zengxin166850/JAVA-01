package com.zengxin.homework0702.config;

import com.zengxin.homework0702.consts.DBTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 将 master、slave都给 RoutingDataSource管理
 */
@Configuration
public class DataSourceConfig {


    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slaveDataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slaveDataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource myRoutingDataSource(DataSource masterDataSource, DataSource slaveDataSource1, DataSource slaveDataSource2) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE1, slaveDataSource1);
        targetDataSources.put(DBTypeEnum.SLAVE2, slaveDataSource2);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

}