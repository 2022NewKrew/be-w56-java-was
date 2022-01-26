package user.controller;

import db.DataBase;
import dto.ControllerDTO;
import http.Request;
import model.User;

import java.util.Map;

public class UserController {

    //회원가입
    public static String execute(ControllerDTO controllerDTO) {
        Map<String, String> elements = controllerDTO.getElement();

        User user = new User(elements.get("userId"),
                elements.get("password"),
                elements.get("name"),
                elements.get("email"));

        DataBase.addUser(user);

        return "/index.html";
    }
}
