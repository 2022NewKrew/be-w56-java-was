package dto;

import util.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class UserLoginRequest {

    private final String userId;
    private final String password;

    private UserLoginRequest(Map<String, List<String>> parameterMap) {
        validateParams(parameterMap);
        this.userId = parameterMap.get("userId").get(0);
        this.password = parameterMap.get("password").get(0);
    }

    public static UserLoginRequest from(String requestBody) {
        Map<String, List<String>> parameterMap = HttpRequestUtils.parseQueryString(requestBody);
        return new UserLoginRequest(parameterMap);
    }

    private void validateParams(Map<String, List<String>> parameterMap) {
        if (
                !parameterMap.containsKey("userId") ||
                !parameterMap.containsKey("password") ||
                parameterMap.get("userId").isEmpty() ||
                parameterMap.get("password").isEmpty()) {
            throw new IllegalArgumentException("요청 파라미터가 존재하지 않습니다.");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
