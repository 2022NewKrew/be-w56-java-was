package application.service;

import application.db.DataBase;
import application.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static application.common.ExceptionMessage.USER_ID_DUPLICATED_EXCEPTION;

@Slf4j
public class UserService {

    public static void create(Map<String, String> parameters) throws IllegalArgumentException {
        log.debug("[회원가입] " + UserService.class + ": UserService.create()");
        String userId = parameters.get("userId");
        if (DataBase.existsUserById(userId)) {
            throw new IllegalArgumentException(USER_ID_DUPLICATED_EXCEPTION.getMessage());
        }

        String password = parameters.get("password");
        String name = parameters.get("name");
        String email = parameters.get("email");
        User user = User.valueOf(userId, password, name, email);
        DataBase.addUser(user);
    }
}
