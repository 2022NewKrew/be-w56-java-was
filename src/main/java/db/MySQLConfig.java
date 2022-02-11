package db;

import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class MySQLConfig {
    public static final MySQLConfig INSTANCE = new MySQLConfig();

    private static final Logger log = LoggerFactory.getLogger(MySQLConfig.class);
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://jayde-spring-test.ay1.krane.9rum.cc:3306/testdb";

    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    private Connection conn = null;

    public MySQLConfig() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);


        } catch (ClassNotFoundException | SQLException e) {
            log.error("DB Connection Error.");
            e.printStackTrace();
        }
    }


    public void addUser(User user) throws SQLException {
        String sql = String.format("INSERT INTO user (USERID, PASSWORD, NAME, EMAIL) VALUES ('%s', '%s', '%s', '%s')", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    public ResultSet findUserByUserId(String userId) throws SQLException {
        String sql = String.format("SELECT id, userId, password, name, email FROM user WHERE userID='%s'", userId);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet findUserById(Long id) throws SQLException {
        String sql = String.format("SELECT id, userId, password, name, email FROM user WHERE ID=%s", id);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public ResultSet getUsers() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM user";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void addMemo(Memo memo) throws SQLException {
        String sql = String.format("INSERT INTO memo (AUTHOR_ID, CONTENT) VALUES (%s, '%s')", memo.getUser().getId(), memo.getContent());
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    public ResultSet getMemos() throws SQLException {
        String sql = "SELECT ID, AUTHOR_ID, CONTENT, createDATE FROM memo";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}
