package model;

public class UserLogin {
    private final String userId;
    private final String password;

    public UserLogin(String userId, String password) {
        this.userId = userId;
        this.password = password;
        validate();
    }

    public void validate() {
        if (userId.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 로그인을 위한 사용자 정보가 올바르지 않습니다.");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserLogin [userId=" + userId + ", password=" + password + "]";
    }
}
