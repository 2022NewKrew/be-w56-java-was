package app.user.domain;

public class User {

    private final UserId userId;
    private final String password;
    private final String name;
    private final String email;

    public User(UserId userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean isPasswordMatch(String tryPassword) {
        return this.password.equals(tryPassword);
    }

    public UserId getUserId() {
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

}
