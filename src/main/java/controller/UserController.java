package controller;

import db.DataBase;
import model.User;
import util.annotation.Controller;
import util.annotation.GetMapping;

import java.util.Map;

@Controller
public class UserController {

    @GetMapping(url="/user/create")
    public String signInByGet(Map<String, String> params){
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        DataBase.addUser(user);
        return "/index.html";
    }

}
