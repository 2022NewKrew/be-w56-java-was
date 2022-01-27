package service;

import db.DataBase;
import model.User;

import java.util.Map;
import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE;

    static {
        INSTANCE = new UserService();
    }

    private UserService() {}

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void createUser(Map<String, String> queryParams) {
        User newUser = User.builder()
                .userId(queryParams.get("userId"))
                .password(queryParams.get("password"))
                .name(queryParams.get("name"))
                .email(queryParams.get("email"))
                .build();

        DataBase.addUser(newUser);
    }

    public void loginUser(Map<String, String> bodyParams) {
        String userId = bodyParams.get("userId");
        String password = bodyParams.get("password");
        User user = Optional.ofNullable(DataBase.findUserById(userId))
                            .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if(!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }
}
