package dto;

import http.request.RequestBody;

public class UserLoginDto {

    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";

    private final String userId;
    private final String password;

    private UserLoginDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public static UserLoginDto from(RequestBody requestBody) {
        String userId = requestBody.getValue(USER_ID);
        String password = requestBody.getValue(PASSWORD);

        return new UserLoginDto(userId, password);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
