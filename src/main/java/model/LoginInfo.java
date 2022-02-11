package model;

public class LoginInfo {
    private final String userId;
    private final String password;

    public LoginInfo(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public boolean authorize(User user) {
        return !user.isDummyUser() && password.equals(user.getPassword());
    }
}
