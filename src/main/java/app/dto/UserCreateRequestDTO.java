package app.dto;

import lombok.Builder;
import lombok.Getter;

import app.model.User;

@Builder
@Getter
public class UserCreateRequestDTO {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
