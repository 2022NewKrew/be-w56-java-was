package domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    @Builder
    public UserCreateRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
