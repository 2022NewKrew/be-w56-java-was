package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        if(connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://10.202.180.9:3306/theodb?autoReconnect=true&validationQuery=select 1", "theo", "password");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void close() {
        try {
            if ( connection != null && !connection.isClosed() ) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }
}
