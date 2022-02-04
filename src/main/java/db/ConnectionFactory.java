package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private static MysqlDataSource DATA_SOURCE;

    public static Connection createConnection(String url, String user, String password) throws SQLException {
        if (DATA_SOURCE == null) {
            DATA_SOURCE = new MysqlDataSource();
            DATA_SOURCE.setUrl(url);
            DATA_SOURCE.setUser(user);
            DATA_SOURCE.setPassword(password);
        }
        return DATA_SOURCE.getConnection();
    }
}
