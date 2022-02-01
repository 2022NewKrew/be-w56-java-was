package configure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfigure {

    private static final String url = "jdbc:mysql://localhost:3306/JAVA_WAS";
    private static final String user = "root";
    private static final String password = "test";

    private final Connection connection;

    public DbConfigure() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }
}
