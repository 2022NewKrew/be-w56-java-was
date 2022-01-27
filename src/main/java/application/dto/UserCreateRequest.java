package application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class UserCreateRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

    public static UserCreateRequest mapToUserCreateRequest(Map<String, String> httpRequestBody) {
        String userId = httpRequestBody.get("userId");
        String password = httpRequestBody.get("password");
        String name = httpRequestBody.get("name");
        String email = httpRequestBody.get("email");

        return new UserCreateRequest(userId, password, name, email);
    }
}
