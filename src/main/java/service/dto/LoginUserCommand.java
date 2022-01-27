package service.dto;

public class LoginUserCommand {

    private final String userId;
    private final String password;

    public LoginUserCommand(String userId, String password) {
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
