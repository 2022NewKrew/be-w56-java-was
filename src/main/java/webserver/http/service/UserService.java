package webserver.http.service;

import db.DataBase;
import dto.UserLoginDto;
import dto.UserSignUpDto;
import entity.User;

import java.util.Collection;
import java.util.List;

public class UserService {

    public void join(UserSignUpDto userSignUpDto) {
        User user = userSignUpDto.toEntity();
        DataBase.addUser(user);
    }

    public User findUser(UserLoginDto userLoginDto) {
        return DataBase.findUserById(userLoginDto.getUserId());
    }

    public Collection<User> findUsers() {
        return DataBase.findAll();
    }
}
