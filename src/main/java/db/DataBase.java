package db;

import java.sql.*;
import java.util.*;

import com.google.common.collect.Maps;

import model.User;
import util.PropertiesUtils;

public class DataBase {
    private static Connection connection;

    public static void connect(){
        Properties prop = PropertiesUtils.readDBProperties();
        try{
            assert prop != null;
            String url = prop.getProperty("url");
            String userName = prop.getProperty("userName");
            String password = prop.getProperty("password");
            connection = DriverManager.getConnection(url, userName, password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void close() throws SQLException {
        connection.close();
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO USERS(userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
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
