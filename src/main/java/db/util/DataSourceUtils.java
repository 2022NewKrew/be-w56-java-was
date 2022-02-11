package db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);

    private static final String SERVER_HOST = "127.0.0.1:3306";
    private static final String DATABASE = "onboardingWas";
    private static final String USER_NAME = "good";
    private static final String PASSWORD = "good1234";

    private static final String CONNECTION_URL = String.format("jdbc:mysql://%s/%s?useSSL=false", SERVER_HOST, DATABASE);

    public static Connection getConnection() throws SQLException {
        return createConnection();
    }

    private static Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
        log.info("Database에 정상적으로 연결되었습니다.");
        return con;
    }
}
