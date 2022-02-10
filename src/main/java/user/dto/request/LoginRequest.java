package user.dto.request;

import user.domain.User;

public class LoginRequest {

    private final String userId;
    private final String password;

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User toUser() {
        return User.builder()
            .userId(this.userId)
            .password(this.password)
            .build();
    }
}
