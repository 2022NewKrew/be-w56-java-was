package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/javawas?useSSL=false";
        String userName = "root";
        String password = "kakao1234";
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
