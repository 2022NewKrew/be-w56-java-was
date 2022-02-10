package dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnectionMaker implements ConnectionMaker {
    private Connection connection;

    @Override
    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://test-cafe-ed.ay1.krane.9rum.cc:3306/javawas",
                        "ed3",
                        "Ed1q2w3e4r!"
                );
            } catch (SQLException e) {
                // TODO
            } catch (ClassNotFoundException e) {
                // TODO
            }
        }
        return connection;
    }
}
