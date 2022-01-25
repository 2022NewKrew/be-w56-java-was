package model;

import org.apache.commons.lang3.StringUtils;
import webserver.exception.InvalidInputException;

public class UserSignupRequest {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public UserSignupRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void validate() {
        validateBlank(userId);
        validateBlank(password);
        validateBlank(name);
        validateBlank(email);
        validateEmail(email);
    }

    private void validateBlank(String param) {
        if (StringUtils.isBlank(param)) {
            throw new InvalidInputException("공백을 입력하였습니다.");
        }
    }

    private void validateEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if (!email.matches(regex)) {
            throw new InvalidInputException("이메일 형식이 잘못되었습니다.");
        }
    }

    public User toUser() {
        return new User(userId, password, name, email);
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
