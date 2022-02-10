package db;

import model.User;
import util.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection connection;

    private UserRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    private static class LazyHolder {
        private static final UserRepository userRepository = new UserRepository(DBUtils.getConnection());
    }

    public static UserRepository getInstance() {
        return LazyHolder.userRepository;
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS USERS (" +
                "USER_ID VARCHAR(20) NOT NULL PRIMARY KEY," +
                "PASSWORD VARCHAR(20) NOT NULL," +
                "NAME VARCHAR(10) NOT NULL," +
                "EMAIL VARCHAR(50) NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO USERS(USER_ID, PASSWORD, NAME, EMAIL) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserById(String userId) {
        String sql = "SELECT USER_ID, PASSWORD, NAME, EMAIL FROM USERS WHERE USER_ID = ?";
        User findUser = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            findUser = new User(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return findUser;
    }

    public List<User> findAll() {
        String sql = "SELECT USER_ID, PASSWORD, NAME, EMAIL FROM USERS";
        List<User> users = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
