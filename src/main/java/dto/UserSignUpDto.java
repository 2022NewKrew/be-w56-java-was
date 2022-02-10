package dto;

import entity.User;

public class UserSignUpDto {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public UserSignUpDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .build();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
