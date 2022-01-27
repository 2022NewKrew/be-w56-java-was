package application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class UserCreateRequest {
    private String userId;
    private String password;
    private String name;
    private String email;

    public static UserCreateRequest mapToUserCreateRequest(Map<String, String> body) {
        return UserCreateRequest.builder()
                .userId(body.get("userId"))
                .password(body.get("password"))
                .name(body.get("name"))
                .email(body.get("email"))
                .build();
    }
}
