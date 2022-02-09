package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.url, DBConfig.userName, DBConfig.password);
    }
}
