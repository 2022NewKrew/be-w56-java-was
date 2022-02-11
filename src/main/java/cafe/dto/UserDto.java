package cafe.dto;

import cafe.model.User;

public class UserDto {
    private String userId;
    private String name;
    private String email;

    public UserDto(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserDto of(User user) {
        return new UserDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
