package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import model.User;
import webserver.SingletonBeanRegistry;

import javax.sql.DataSource;

public class DataBase {
    private static final DataSource dataSource = (DataSource) SingletonBeanRegistry.getBean("DataSource");

    public static void addUser(User user) {
        String insertUser = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?);";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(insertUser);
            stat.setString(1, user.getUserId());
            stat.setString(2, user.getPassword());
            stat.setString(3, user.getName());
            stat.setString(4, user.getEmail());
            stat.executeUpdate();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        String selectUser = "SELECT user_id, password, name, email FROM users WHERE user_id = ?;";
        User user = null;
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(selectUser);
            stat.setString(1, userId);
            ResultSet rs = stat.executeQuery();
            user = new User(
                    rs.getString("user_id"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
            );
            rs.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static Collection<User> findAll() {
        String selectUser = "SELECT user_id, password, name, email FROM users;";
        Collection<User> users = new ArrayList<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(selectUser);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
