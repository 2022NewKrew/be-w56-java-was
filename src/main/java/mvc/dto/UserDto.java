package mvc.dto;

import lombok.*;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDto(Map<String, String> params) {
        userId = params.get("userId");
        password = params.get("password");
        name = params.get("name");
        email = params.get("email");
    }
}
