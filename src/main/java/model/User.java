package model;

import java.util.Map;

import static db.DataBase.addUser;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> userMap){
        this.userId = userMap.get("userId");
        this.password = userMap.get("password");
        this.name = userMap.get("name");
        this.email = userMap.get("email");
    }

    public static User join(Map<String, String> userMap){
        User user = new User(userMap);
        addUser(user);
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
