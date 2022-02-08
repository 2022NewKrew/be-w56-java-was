package dao;

import model.User;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 9:03
 */
public class UserDao {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDao() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
