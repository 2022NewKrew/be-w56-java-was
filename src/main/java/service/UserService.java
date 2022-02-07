package service;

import db.DataBase;
import exception.PasswordMissMatchException;
import exception.UserNotFoundException;
import model.User;

import java.util.Collection;

public class UserService {

    public void login(String userId, String password) throws UserNotFoundException, PasswordMissMatchException {
        User user = findByUserId(userId);
        user.validateEqualsPassword(password);
    }

    public User findByUserId(String userId) throws UserNotFoundException {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("해당 유저가 존재하지 않습니다.");
        }
        return user;
    }

    public Collection<User> findAllUser() {
        return DataBase.findAll();
    }
}
