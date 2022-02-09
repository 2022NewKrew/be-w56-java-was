package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JdbcConfig {

    private static final String DATABASE_URL = "jdbc:h2:mem:db";
    private static final String DATABASE_USERNAME = "sa";
    private static final String DATABASE_PASSWORD = "pwd";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME,
                DATABASE_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JdbcConfig() {

    }

    public static Connection getConnection() {
        return connection;
    }
}
