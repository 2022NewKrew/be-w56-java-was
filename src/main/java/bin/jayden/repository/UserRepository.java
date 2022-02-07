package bin.jayden.repository;

import bin.jayden.db.MyJDBC;
import bin.jayden.db.MyRowMapper;
import bin.jayden.model.User;

import java.util.List;

public class UserRepository {
    private final MyJDBC jdbc;

    public UserRepository(MyJDBC jdbc) {
        this.jdbc = jdbc;
    }

    public int insertUser(User user) {
        return jdbc.update("insert into User(userId, password, name, email) values ('" + user.getUserId() + "','" + user.getPassword() + "','" + user.getPassword() + "','" + user.getEmail() + "')");
    }

    public List<User> getUserList() {
        return jdbc.queryObjectList("Select id,userId,password,name,email from User", getUserRowMapper());
    }


    public User getUser(String userId, String password) {
        return jdbc.queryObject("SELECT id,userId,password,name,email from User where userId = " + userId + " and password = " + password, getUserRowMapper());
    }

    private MyRowMapper<User> getUserRowMapper() {
        return resultSet -> new User(
                resultSet.getLong("id"),
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }
}
