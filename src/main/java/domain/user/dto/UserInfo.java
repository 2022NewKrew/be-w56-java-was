package domain.user.dto;

import domain.user.model.User;
import lombok.Getter;

@Getter
public class UserInfo {

    private final String userId;
    private final String name;
    private final String email;

    public static UserInfo from(User user) {
        return new UserInfo(user.getUserId(), user.getName(), user.getEmail());
    }

    private UserInfo(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}
