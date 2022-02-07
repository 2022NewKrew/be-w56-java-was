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

        public static User findUserById(String userId) throws SQLException {
            Connection connection = DbUtils.getDbConnection();

            PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_QUERY);
            pstmt.setString(1,userId);
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

        public static void save(Request request) throws SQLException {
            Map<String, String> queryString = request.getQueryString();
            Connection connection = DbUtils.getDbConnection();

            PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, queryString.get("userId"));
            pstmt.setString(2, queryString.get("password"));
            pstmt.setString(3, queryString.get("name"));
            pstmt.setString(4, queryString.get("email"));
            int result = pstmt.executeUpdate();

            pstmt.close();
            connection.close();
        }

        public static List<User> findAll() throws SQLException {
            List<User> userList = new ArrayList<>();
            Connection connection = DbUtils.getDbConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);

            while(resultSet.next()) {
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
