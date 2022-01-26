package model;

public class User {
    private final UserId userId;
    private final Password password;
    private final Name name;
    private final Email email;

    public User(UserId userId, Password password, Name name, Email email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId.getUserId();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public String getName() {
        return name.getName();
    }

    public String getEmail() {
        return email.getEmail();
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
