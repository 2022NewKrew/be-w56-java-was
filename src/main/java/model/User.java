package model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.bson.types.ObjectId;

public class User extends BaseTime {

    private final ObjectId id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(ObjectId id, String userId, String password, String name, String email,
            LocalDateTime createTime, LocalDateTime modifiedTime) {
        super(createTime, modifiedTime);

        checkId(id);
        checkUserId(userId);
        checkPassword(password);
        checkName(name);
        checkEmail(email);

        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(ObjectId id, String userId, String password, String name, String email,
            LocalDateTime time) {
        this(id, userId, password, name, email, time, time);
    }

    public User(String userId, String password, String name, String email) {
        this(new ObjectId(), userId, password, name, email, LocalDateTime.now(ZoneOffset.UTC));
    }

    public User(ObjectId id, String userId, String password, String name, String email) {
        this(id, userId, password, name, email, LocalDateTime.now(ZoneOffset.UTC));
    }

    private void checkId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("illegal id");
        }
    }

    private void checkUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("illegal UserId");
        }
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("illegal Password");
        }
    }

    private void checkName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("illegal Name");
        }
    }

    private void checkEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("illegal Email");
        }
    }

    public ObjectId getId() {
        return id;
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
