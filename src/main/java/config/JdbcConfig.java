package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig {

    private static final String DATABASE_URL = "jdbc:h2:mem:db";
    private static final String DATABASE_USERNAME = "sa";
    private static final String DATABASE_PASSWORD = "";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME,
                    DATABASE_PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
