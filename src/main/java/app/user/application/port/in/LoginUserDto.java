package app.user.application.port.in;

import app.user.domain.UserId;

public class LoginUserDto {

    private final UserId userId;
    private final String password;

    public LoginUserDto(UserId userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
