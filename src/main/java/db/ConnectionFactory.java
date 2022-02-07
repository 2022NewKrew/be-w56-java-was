package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private static MysqlDataSource dataSource;

    public static Connection createConnection(String url, String user, String password) throws SQLException {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
        }
        return dataSource.getConnection();
    }
}
