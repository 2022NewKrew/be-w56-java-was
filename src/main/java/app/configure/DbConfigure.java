package app.configure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfigure {

    private static final String URL = "jdbc:mysql://localhost:3306/JAVA_WAS";
    private static final String USER = "root";
    private static final String PASSWORD = "test";

    private final Connection connection;

    public DbConfigure() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }
}
