package service;

import db.MySQLConfig;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserService {
    public static final UserService INSTANCE = new UserService();

    private final MySQLConfig mySQLConfig;

    private UserService() {
        this.mySQLConfig = MySQLConfig.INSTANCE;
    }

    public void addUser(User user) throws SQLException {
        mySQLConfig.addUser(user);
    }

    public boolean userLogin(String userId, String password) throws SQLException {
        ResultSet rs = mySQLConfig.findUserByUserId(userId);
        return rs.next() && rs.getString("password").equals(password);
    }

    public String getUserList() throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = mySQLConfig.getUsers();
        int idx = 1;
        while (rs.next()) {
            sb.append("<tr><th scope=\"row\">" + idx + "</th>" +
                    "<td>" + rs.getString("userid") + "</td>" +
                    "<td>" + rs.getString("name") + "</td>" +
                    "<td>" + rs.getString("email") + "</td>" +
                    "<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
            idx++;
        }
        return sb.toString();
    }

    public User getUserById(long id) throws SQLException {
        ResultSet rs = mySQLConfig.findUserById(id);
        rs.next();
        return new User(rs.getInt("ID"), rs.getString("USERID"), rs.getString("PASSWORD"), rs.getString("NAME"), rs.getString("EMAIL"));
    }

    public User getUserByUserId(String userId) throws SQLException {
        ResultSet rs = mySQLConfig.findUserByUserId(userId);
        rs.next();
        return new User(rs.getInt("ID"), rs.getString("USERID"), rs.getString("PASSWORD"), rs.getString("NAME"), rs.getString("EMAIL"));
    }
}
