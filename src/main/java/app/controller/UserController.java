package app.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.annotation.Controller;
import app.annotation.GetMapping;
import app.annotation.PostMapping;
import app.db.DataBase;
import app.model.user.User;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/")
    public String index() {
        return "/index.html";
    }

    @PostMapping(value = "/user")
    public String addUser(Map<String, String> body) {
        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        DataBase.addUser(user);
        log.debug("add User: {} {} {} {}", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        return "redirect:/index.html";
    }

    @PostMapping(value = "/user/login")
    public String login(Map<String, String> body) {
        String userId = body.get("userId");
        String password = body.get("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.validatePassword(password)) {
            return "redirect:/user/login_failed.html";
        }
//        httpResponse.setCookie("logined", true);
//        httpResponse.setCookie("Path", "/");
//        httpResponse.setCookie("max-age", 100);
        return "redirect:/index.html";
    }
}
