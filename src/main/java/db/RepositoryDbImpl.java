package db;

import model.User;

import java.sql.*;
import java.util.*;

public class RepositoryDbImpl {
    public static void main(String[] args) throws SQLException {
        List<User> userList = new ArrayList<>();

        String url = "jdbc:mysql://muscle-db.ay1.krane.9rum.cc:3306/test";
        String userName = "root";
        String password = "1234";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from USER");

        while(resultSet.next()) {
            // user 객체에 값 저장
            User newUser = User.builder()
                    .id(resultSet.getLong(1))
                    .userId(resultSet.getString(2))
                    .password(resultSet.getString(3))
                    .name(resultSet.getString(4))
                    .email(resultSet.getString(5))
                    .build();

            // 리스트에 추가
            userList.add(newUser);
        }
        System.out.println(userList);
//        resultSet.next();
//        String name = resultSet.getString("name");
//        System.out.println(name);

        resultSet.close();
        statement.close();
        connection.close();
    }
}
