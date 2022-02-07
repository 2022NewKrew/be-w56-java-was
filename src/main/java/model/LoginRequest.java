package model;

import java.util.Map;

public class LoginRequest {

    private final String userId;
    private final String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public static LoginRequest from(Map<String, String> queryData) {
        String userId = queryData.get("userId");
        String password = queryData.get("password");

        return new LoginRequest(userId, password);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
