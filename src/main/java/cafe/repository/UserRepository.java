package cafe.repository;

import cafe.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserRepository {
    private static final String DB_URL = "jdbc:mysql://10.202.174.226/spring_cafe";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root123";

    public void addUser(User user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement();
        ){
            String query = "INSERT INTO USER_PROFILE(user_id, password, name, email) " +
                    "VALUE('" + user.getUserId() + "', '" + user.getPassword() + "', '" + user.getName() + "', '" + user.getEmail() + "')";

            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserById(String userId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement();
        ){
            String query = "SELECT user_id, password, name, email FROM USER_PROFILE WHERE user_id = '" + userId + "'";

            ResultSet resultSet = statement.executeQuery(query);
            User user = null;
            while (resultSet.next()) {
                user = new User(resultSet.getString("user_id"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"));
            }
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public Collection<User> findAll() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement();
        ){
            String query = "SELECT user_id, password, name, email FROM USER_PROFILE";

            ResultSet resultSet = statement.executeQuery(query);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("user_id"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email")));
            }
            return users;
        } catch (SQLException e) {
            return null;
        }
    }
}
