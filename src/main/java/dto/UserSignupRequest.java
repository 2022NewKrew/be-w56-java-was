package dto;

public class UserSignupRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public UserSignupRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
