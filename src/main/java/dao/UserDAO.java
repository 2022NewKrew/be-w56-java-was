package dao;

import dto.UserCreateCommand;
import model.User;
import util.DBUtils;
import webserver.http.HttpRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private static final String STORE_SQL =
            "INSERT INTO USERS(USER_ID, PASSWORD, NAME, EMAIL) VALUES(?, ?, ?, ?)";
    private static final String SEARCH_SQL =
            "SELECT * FROM USERS WHERE USER_ID=?";
    private static final String TO_LIST_SQL =
            "SELECT * FROM USERS";

    private final Connection connection;

    public UserDAO() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("DB connection failed");
        }
    }

    public User findUser(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SEARCH_SQL);
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        User target = User.builder()
                .userId(resultSet.getString(1))
                .password(resultSet.getString(2))
                .name(resultSet.getString(3))
                .email(resultSet.getString(4))
                .build();

        resultSet.close();
        statement.close();
        return target;
    }

    public void storeUser(UserCreateCommand ucc) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(STORE_SQL);
        statement.setString(1, ucc.getUserId());
        statement.setString(2, ucc.getPassword());
        statement.setString(3, ucc.getName());
        statement.setString(4, URLDecoder.decode(ucc.getEmail(), StandardCharsets.UTF_8));
        statement.execute();

        statement.close();
    }

    public List<User> findAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(TO_LIST_SQL);

        while (resultSet.next()) {
            User user = User.builder()
                    .userId(resultSet.getString(1))
                    .password(resultSet.getString(2))
                    .name(resultSet.getString(3))
                    .email(resultSet.getString(4))
                    .build();
            userList.add(user);
        }

        resultSet.close();
        statement.close();
        return userList;
    }
}
