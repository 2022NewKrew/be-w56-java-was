package dto;

import http.request.RequestBody;
import model.User;

public class UserCreateDto {

    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private UserCreateDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static UserCreateDto from(RequestBody requestBody) {
        String userId = requestBody.getValue(USER_ID);
        String password = requestBody.getValue(PASSWORD);
        String name = requestBody.getValue(NAME);
        String email = requestBody.getValue(EMAIL);

        return new UserCreateDto(userId, password, name, email);
    }

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
