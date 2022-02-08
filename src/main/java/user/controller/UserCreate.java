package user.controller;

import controller.AbstractController;
import db.DataBase;
import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserCreate extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserCreate.class);

    //회원가입
    @Override
    public String controllerExecute(Request request) {
        Map<String, String> elements = request.getElements();

        User user = new User(elements.get("userId"),
                elements.get("password"),
                elements.get("name"),
                elements.get("email"));

        if(DataBase.findUserById(user.getUserId()) != null){
            return "redirect:/index.html";
        }

        DataBase.addUser(user);

        log.debug(String.format("new user registered : %s", user.getUserId()));
        return "redirect:/index.html";
    }
}
