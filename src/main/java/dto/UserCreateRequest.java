package dto;

import model.User;

import java.util.Map;

public class UserCreateRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private UserCreateRequest(Map<String, String> parameterMap) {
        validateParams(parameterMap);
        this.userId = parameterMap.get("userId");
        this.password = parameterMap.get("password");
        this.name = parameterMap.get("name");
        this.email = parameterMap.get("email");
    }

    public static UserCreateRequest of(Map<String, String> parameterMap) {
        return new UserCreateRequest(parameterMap);
    }

    private void validateParams(Map<String, String> parameterMap) {
        if (
                !parameterMap.containsKey("userId") ||
                !parameterMap.containsKey("password") ||
                !parameterMap.containsKey("name") ||
                !parameterMap.containsKey("email")) {
            throw new IllegalArgumentException("요청 파라미터가 존재하지 않습니다.");
        }
    }

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
