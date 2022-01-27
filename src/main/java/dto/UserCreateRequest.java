package dto;

import model.User;
import util.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class UserCreateRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private UserCreateRequest(Map<String, List<String>> parameterMap) {
        validateParams(parameterMap);
        this.userId = parameterMap.get("userId").get(0);
        this.password = parameterMap.get("password").get(0);
        this.name = parameterMap.get("name").get(0);
        this.email = parameterMap.get("email").get(0);
    }

    public static UserCreateRequest from(String requestBody) {
        Map<String, List<String>> parameterMap = HttpRequestUtils.parseQueryString(requestBody);
        return new UserCreateRequest(parameterMap);
    }

    private void validateParams(Map<String, List<String>> parameterMap) {
        if (
                !parameterMap.containsKey("userId") ||
                !parameterMap.containsKey("password") ||
                !parameterMap.containsKey("name") ||
                !parameterMap.containsKey("email") ||
                parameterMap.get("userId").isEmpty() ||
                parameterMap.get("password").isEmpty() ||
                parameterMap.get("name").isEmpty() ||
                parameterMap.get("email").isEmpty()) {
            throw new IllegalArgumentException("요청 파라미터가 존재하지 않습니다.");
        }
    }

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
