package service;

import db.DataBase;
import dto.UserSignUpDto;
import entity.User;

public class UserService {

    public void join(UserSignUpDto userSignUpDto) {
        User user = new User.Builder(
                userSignUpDto.getUserId(),
                userSignUpDto.getPassword(),
                userSignUpDto.getName(),
                userSignUpDto.getEmail()).build();

        DataBase.addUser(user);
    }
}
