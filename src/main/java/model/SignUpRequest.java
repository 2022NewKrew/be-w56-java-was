package model;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SignUpRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public SignUpRequest(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static SignUpRequest from(Map<String, String> queryData) {
        String userId = queryData.get("userId");
        String password = queryData.get("password");
        String name = queryData.get("name");
        String email = URLDecoder.decode(queryData.get("email"), StandardCharsets.UTF_8);

        return new SignUpRequest(userId, password, name, email);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User toUser() {
        return new User(this.userId, this.password, this.name, this.email);
    }
}
