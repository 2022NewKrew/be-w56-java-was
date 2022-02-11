package cafe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://10.202.174.226/spring_cafe";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root123";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
