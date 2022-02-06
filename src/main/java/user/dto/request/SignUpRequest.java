package user.dto.request;

import java.time.LocalDateTime;
import user.domain.User;

public class SignUpRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public SignUpRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toUser() {
        return User.builder()
            .userId(this.userId)
            .password(this.password)
            .name(this.name)
            .email(this.email)
            .createdDate(LocalDateTime.now())
            .build();
    }
}
