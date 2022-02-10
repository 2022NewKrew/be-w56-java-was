package user.service;

import common.exception.AuthException;
import common.exception.ErrorCode;
import java.util.Optional;
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

    public boolean login(User loginUser) {
        Optional<User> user = DataBase.findUserByUserId(loginUser.getUserId());
        if (user.isEmpty()) {
            return false;
        }
        return isCorrectPassword(user.get(), loginUser);
    }

    private boolean isCorrectPassword(User user, User loginUser) {
        return user.getPassword().equals(loginUser.getPassword());
    }
}
