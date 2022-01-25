package model;

public class UserSignupRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserSignupRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toUser() {
        return new User(userId, password, name, email);
    }

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
