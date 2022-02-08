package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.sql.*;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DataBaseUtils;

public class DataBase {

    private static final Logger log = LoggerFactory.getLogger(DataBase.class);

    public static void addUser(User user) {
        try {
            Connection connection = DataBaseUtils.connectDB();

            String sql = "INSERT INTO user (userId, password, name, email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        try {
            Connection connection = DataBaseUtils.connectDB();

            String sql = "SELECT userId, password, name, email FROM user WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user;
            if (resultSet.next()) {
                String id = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                user = new User(id, password, name, email);
            } else {
                log.debug("not exist user");
                throw new SQLException("not exist user");
            }
            preparedStatement.close();
            connection.close();

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<User> findAll() {
        try {
            Connection connection = DataBaseUtils.connectDB();

            List<User> users = new ArrayList<>();
            String sql = "SELECT userId, password, name, email FROM user";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String id = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                User user = new User(id, password, name, email);
                users.add(user);
                log.debug(user.toString());
            }
            statement.close();
            connection.close();

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
