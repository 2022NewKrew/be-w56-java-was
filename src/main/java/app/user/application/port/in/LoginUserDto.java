package app.user.application.port.in;

public class LoginUserDto {

    private final String userId;
    private final String password;

    public LoginUserDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
