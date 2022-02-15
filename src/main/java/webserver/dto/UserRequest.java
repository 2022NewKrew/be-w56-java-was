package webserver.dto;

import lombok.Builder;
import lombok.Getter;
import webserver.http.request.InfoMap;

@Getter
public class UserRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    @Builder
    private UserRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static UserRequest from(InfoMap infoMap){
        return UserRequest.builder()
                .userId(infoMap.get("userId"))
                .password(infoMap.get("password"))
                .name(infoMap.get("name"))
                .email(infoMap.get("email"))
                .build();
    }



}
