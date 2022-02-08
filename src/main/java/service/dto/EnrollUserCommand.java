package service.dto;

import domain.User;

public class EnrollUserCommand {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public EnrollUserCommand(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return new User(
                userId,
                password,
                name,
                email);
    }
}
