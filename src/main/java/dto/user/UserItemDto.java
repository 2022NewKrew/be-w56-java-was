package dto.user;

import model.User;

public class UserItemDto {
    private String username;
    private String name;
    private String email;

    public UserItemDto(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public static UserItemDto of(User user) {
        return new UserItemDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
