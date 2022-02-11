package cafe.repository;

import cafe.model.User;
import cafe.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserRepository {
    public void addUser(User user) {
        String query = "INSERT INTO USER_PROFILE(user_id, password, name, email) VALUE(?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserById(String userId) {
        String query = "SELECT user_id, password, name, email FROM USER_PROFILE WHERE user_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        String query = "SELECT user_id, password, name, email FROM USER_PROFILE";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
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
