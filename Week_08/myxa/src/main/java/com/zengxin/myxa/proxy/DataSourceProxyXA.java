package com.zengxin.myxa.proxy;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 将datasource包装为XADataSource
 */
public class DataSourceProxyXA extends AbstractDataSourceProxy {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DataSourceProxyXA.class);

    /**
     * 初始化
     *
     * @param dataSource 普通的datasource
     */
    public DataSourceProxyXA(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        return getConnectionProxy(connection);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = dataSource.getConnection(username, password);
        return getConnectionProxy(connection);
    }

    protected Connection getConnectionProxy(Connection connection) {

        return null;
    }

    protected Connection getConnectionProxyXA() {
        return null;
    }

    private Connection getConnectionProxyXA(Connection connection) throws SQLException {
        Connection unwrap = connection.unwrap(Connection.class);
        return null;
    }
}
