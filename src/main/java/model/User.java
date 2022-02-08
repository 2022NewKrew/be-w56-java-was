package model;

import java.time.LocalDateTime;

public class User extends BaseTime {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email, LocalDateTime createTime,
            LocalDateTime modifiedTime) {
        super(createTime, modifiedTime);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(String userId, String password, String name, String email) {
        this(userId, password, name, email, LocalDateTime.now(), LocalDateTime.now());
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
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email="
                + email + "]";
    }
}
