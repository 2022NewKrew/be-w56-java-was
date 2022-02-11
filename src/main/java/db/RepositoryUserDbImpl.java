package db;

import model.Request;
import model.User;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static query.UserQuery.*;

public class RepositoryUserDbImpl {

    private final Connection connection;

    public RepositoryUserDbImpl() {
        try {
            this.connection = DbUtils.getDbConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("connection fail");
        }
    }

    public User findUserById(String userId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_QUERY);
        pstmt.setString(1, userId);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();

        User findUser = User.builder()
                .id(resultSet.getLong(1))
                .userId(resultSet.getString(2))
                .password(resultSet.getString(3))
                .name(resultSet.getString(4))
                .email(resultSet.getString(5))
                .build();

        resultSet.close();
        pstmt.close();
        connection.close();
        return findUser;
    }

    public void save(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();

        PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
        pstmt.setString(1, queryString.get("userId"));
        pstmt.setString(2, queryString.get("password"));
        pstmt.setString(3, queryString.get("name"));
        pstmt.setString(4, queryString.get("email"));
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);

        while (resultSet.next()) {
            User newUser = User.builder()
                    .id(resultSet.getLong(1))
                    .userId(resultSet.getString(2))
                    .password(resultSet.getString(3))
                    .name(resultSet.getString(4))
                    .email(resultSet.getString(5))
                    .build();

            userList.add(newUser);
        }
        resultSet.close();
        statement.close();
        connection.close();

        return userList;
    }

}
