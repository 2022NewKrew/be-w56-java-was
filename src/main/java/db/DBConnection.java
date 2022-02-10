package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection connection;

    public void connect() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://10.202.171.182:3306/mymemo_db?serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "champ";
        String password = "Kakao123!";
        String driverName = "com.mysql.cj.jdbc.Driver";

        Class.forName(driverName);
        connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
