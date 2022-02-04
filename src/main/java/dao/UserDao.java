package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private Connection connection;
    private Statement statement;

    public UserDao() {
        init();
    }

    public void init() {
        String url = "jdbc:mysql://localhost:3306/javawas?useSSL=false";
        String userName = "root";
        String password = "kakao1234";

        try {
            connection = DriverManager.getConnection(url, userName, password);
            StringBuilder sb = new StringBuilder();
            statement = connection.createStatement();
            String sql = sb.append("CREATE TABLE IF NOT EXISTS users(")
                    .append("id INT AUTO_INCREMENT PRIMARY KEY,")
                    .append("userId VARCHAR(16) NOT NULL,")
                    .append("password VARCHAR(16) NOT NULL,")
                    .append("name VARCHAR(16) NOT NULL,")
                    .append("email VARCHAR(32) NOT NULL")
                    .append(");").toString();

            statement.execute(sql);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        try {
            String sql = "INSERT INTO users(userId, password, name, email) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
        try {
            String sql = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next())
                return null;
            return getUser(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = String.format("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                users.add(getUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUser(ResultSet resultSet) {
        try {
            String userId = resultSet.getString("userId");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            return new User(userId, password, name, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
