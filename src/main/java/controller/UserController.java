package controller;

import model.User;
import service.UserService;

import java.util.Map;

public class UserController implements Controller {
    UserService userService;

    public UserController(){
        this.userService = new UserService();
    }

    @Override
    public String create(Map<String,String> userInfoMap) {
        userService.createUser(userInfoMap);
        return "/index.html";
    }
}
