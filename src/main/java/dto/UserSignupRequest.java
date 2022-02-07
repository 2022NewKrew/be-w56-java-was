package dto;

import model.User;

public class UserSignupRequest {

    private String userId;
    private String password;
    private String name;
    private String email;

    public UserSignupRequest() {
    }

    public UserSignupRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return new User.Builder()
            .userId(userId)
            .password(password)
            .name(name)
            .email(email)
            .build();
    }

    public String getUserId() {
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
