package dto.user;

import model.User;

public class UserSessionedDto {
    private final String username;
    private final String email;
    private final String name;

    public UserSessionedDto(String username, String email, String name) {
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public static UserSessionedDto of(User user) {
        return new UserSessionedDto(user.getUserId(), user.getEmail(), user.getName());
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
