package controller;

import db.DataBase;
import http.MyHttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public String createUser(MyHttpRequest request) {
        Map<String, String> params = request.getParams();
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        log.info("new User (userId : {}, name : {})", user.getUserId(), user.getName());
        return "redirect:/index.html";
    }
}
