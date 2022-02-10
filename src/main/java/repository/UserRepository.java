package repository;

import db.DataBase;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository{

    public static void addUser(User user) {
        String sql = "INSERT INTO USERS(userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DataBase.getConnection().prepareStatement(sql)) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        String sql = "SELECT * FROM USERS WHERE userId=?";
        try (PreparedStatement statement = DataBase.getConnection().prepareStatement(sql)) {
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            User user = new User(resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));
            resultSet.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Statement statement = DataBase.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                userList.add(new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
