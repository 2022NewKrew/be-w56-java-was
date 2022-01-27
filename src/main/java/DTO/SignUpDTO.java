package DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.User;

@AllArgsConstructor
@Getter
public class SignUpDTO {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User makeUser() {
        return new User(userId, password, name, email);
    }
}
