package application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceUtils {

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return DriverManager.getConnection(dataSource.URL, dataSource.USER, dataSource.PASSWORD);
    }

    public static void releaseConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
