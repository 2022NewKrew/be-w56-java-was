package application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class UserLoginRequest {
    private String userId;
    private String password;

    public static UserLoginRequest mapToUserLoginRequest(Map<String, String> body) {
        return UserLoginRequest.builder()
                .userId(body.get("userId"))
                .password(body.get("password"))
                .build();
    }
}
