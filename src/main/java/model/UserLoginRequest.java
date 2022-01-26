package model;

import org.apache.commons.lang3.StringUtils;
import webserver.exception.InvalidInputException;

public class UserLoginRequest {
    private final String userId;
    private final String password;

    public UserLoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public void validate() {
        validateBlank(userId);
        validateBlank(password);
    }

    private void validateBlank(String param) {
        if (StringUtils.isBlank(param)) {
            throw new InvalidInputException("공백을 입력하였습니다.");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
