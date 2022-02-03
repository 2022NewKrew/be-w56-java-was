package dto;

import model.User;

public class UserSignupRequest {

    private String userId;
    private String password;
    private String name;
    private String email;

    public UserSignupRequest() {
    }

    public User toEntity() {
        return new User.Builder()
            .userId(userId)
            .password(password)
            .name(name)
            .email(email)
            .build();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
