package model.auth;

import exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public class LoginRequest {
    private final String userId;
    private final String password;

    public void validate() {
        validateBlank(userId);
        validateBlank(password);
    }

    private void validateBlank(String param) {
        if (StringUtils.isBlank(param)) {
            throw new InvalidInputException("공백을 입력하였습니다.");
        }
    }

}
