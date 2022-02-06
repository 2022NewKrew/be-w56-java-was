package app.user.application.port.in;

import app.user.domain.UserId;

public class SignUpUserDto {

    private final UserId userId;
    private final String password;
    private final String name;
    private final String email;

    public SignUpUserDto(UserId userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public UserId getUserId() {
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
