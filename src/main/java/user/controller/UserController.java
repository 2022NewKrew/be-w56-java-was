package user.controller;

import db.DataBase;
import dto.ControllerDTO;
import model.User;

import java.util.Map;

public class UserController {
    public String createUser(ControllerDTO controllerDTO) {
        Map<String, String> elements = controllerDTO.getElement();

        User user = new User(elements.get("userId"),
                            elements.get("password"),
                            elements.get("name"),
                            elements.get("email"));

        DataBase.addUser(user);

        return "/index.html";
    }
}
