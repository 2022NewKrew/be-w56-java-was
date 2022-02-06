package user.service;

import common.exception.AuthException;
import common.exception.ErrorCode;
import user.domain.User;
import user.repository.DataBase;

public class UserService {

    public void save(User user) {
        validateDuplicateEmail(user);
        DataBase.addUser(user);
    }

    private void validateDuplicateEmail(User user) {
        DataBase.findUserByUserId(user.getUserId())
            .ifPresent(s -> {
                throw new AuthException(ErrorCode.DUPLICATED_USER_ID);
            });
    }
}
