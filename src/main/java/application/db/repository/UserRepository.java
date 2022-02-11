package application.db.repository;

import application.db.DataSource;
import application.db.DataSourceUtils;
import application.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;

        createUserTable();
        User user = new User("testID", "1234", "김민수", "raon.su@kakaocorp.com");
        addUser(user);
    }

    public void addUser(User user) {
        final String sql = "INSERT INTO users VALUES(?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

    }

    public User findUserById(String userId) {
        final String sql = "SELECT * FROM users WHERE userId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            rs.next();
            return userMapper(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM users";

        Connection conn = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                users.add(userMapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User userMapper(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
    }

    private void createUserTable() {
        Connection conn = null;
        Statement statement = null;

        final String sql = "drop table if exists users CASCADE;" +
                " CREATE TABLE users (" +
                " userId VARCHAR(125) PRIMARY KEY," +
                " password VARCHAR(125) NOT NULL," +
                " name VARCHAR(125) NOT NULL," +
                " email VARCHAR(125) NOT NULL" +
                ");";

        try {
            conn = getConnection();
            statement = conn.createStatement();
            statement.execute(sql);
            statement.close();
            DataSourceUtils.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            close(conn);
        }
    }

    private void close(Connection conn) {
        DataSourceUtils.releaseConnection(conn);
    }

}
