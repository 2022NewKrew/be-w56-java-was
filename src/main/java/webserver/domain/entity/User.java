package webserver.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public boolean matchedBy(String password){
        return this.password.equals(password);
    }
}
