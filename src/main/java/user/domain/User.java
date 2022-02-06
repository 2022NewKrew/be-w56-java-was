package user.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;
    private final LocalDateTime createdDate;

    @Builder
    public User(Long id, String userId, String password, String name, String email, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", createdDate=" + createdDate +
            '}';
    }
}
