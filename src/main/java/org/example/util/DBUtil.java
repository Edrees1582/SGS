package org.example.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBUtil {
    private final DataSource dataSource;
    public DBUtil() {
        try {
            dataSource = initializeDataSource();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource initializeDataSource() throws SQLException {
        MysqlDataSource mysqlDataSource =  new MysqlDataSource();
        mysqlDataSource.setServerName("localhost");
        mysqlDataSource.setDatabaseName("sgs");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setAllowPublicKeyRetrieval(true);

        return mysqlDataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
