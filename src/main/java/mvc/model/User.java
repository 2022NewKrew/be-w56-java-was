package mvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
