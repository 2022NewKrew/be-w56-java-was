package domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLogin {

    private final String userId;
    private final String password;

    @Builder
    public UserLogin(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
