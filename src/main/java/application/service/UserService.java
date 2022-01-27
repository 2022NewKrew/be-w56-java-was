package application.service;

import application.db.DataBase;
import application.dto.UserCreateRequest;
import application.model.User;
import lombok.extern.slf4j.Slf4j;

import static application.common.ExceptionMessage.USER_ID_DUPLICATED_EXCEPTION;

@Slf4j
public class UserService {

    public static void create(UserCreateRequest request) throws IllegalArgumentException {
        log.debug("회원가입 - [" + UserService.log.getName() + "] create()");
        String userId = request.getUserId();
        if (DataBase.existsUserById(userId)) {
            throw new IllegalArgumentException(USER_ID_DUPLICATED_EXCEPTION.getMessage());
        }

        User user = User.valueOf(userId, request.getPassword(), request.getName(), request.getEmail());
        DataBase.addUser(user);
    }
}
