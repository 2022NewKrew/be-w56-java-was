package dto;

import model.User;

public class UserProfileDto {

    private final String userId;
    private final String name;
    private final String email;

    private UserProfileDto(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserProfileDto from(User user) {
        String userId = user.getUserId();
        String name = user.getName();
        String email = user.getEmail();

        return new UserProfileDto(userId, name, email);
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
