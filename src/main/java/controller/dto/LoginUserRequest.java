package controller.dto;

public class LoginUserRequest {

    private String userId;
    private String password;

    public LoginUserRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
