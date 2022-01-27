package application.dto;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class UserCreateRequest {
    public String userId;
    public String password;
    public String name;
    public String email;

    public static UserCreateRequest mapToUserCreateRequest(Map<String, String> httpRequestBody) {
        String userId = httpRequestBody.get("userId");
        String password = httpRequestBody.get("password");
        String name = httpRequestBody.get("name");
        String email = httpRequestBody.get("email");

        return new UserCreateRequest(userId, password, name, email);
    }
}
