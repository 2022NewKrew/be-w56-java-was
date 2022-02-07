package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private static String url = "jdbc:mysql://muscle-db.ay1.krane.9rum.cc:3306/test";
    private static String userName = "root";
    private static String password = "1234";

    public static Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

}
