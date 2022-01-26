package model.user;

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

    public UserId getUserId() {
        return userId;
    }

    public Password getPassword() {
        return password;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
