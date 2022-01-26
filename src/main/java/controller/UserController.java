package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.Controller;
import util.annotation.GetMapping;
import util.annotation.PostMapping;
import util.http.HttpResponse;

import java.util.Map;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @GetMapping(url = "/")
    public String index() {
        return "/index.html";
    }

    @GetMapping(url = "/user/create")
    public String signInByGet(Map<String, String> params) {
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        DataBase.addUser(user);
        return "/index.html";
    }

    @PostMapping(url="/user/create")
    public String signInByPost(Map<String, String> body){
        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @PostMapping(url = "/user/login")
    public String login(Map<String, String> body, HttpResponse httpResponse) {
        String userId = body.get("userId");
        String password = body.get("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.passwordIs(password))
            return "redirect:/user/login_failed.html";
        httpResponse.setCookie("logined", true);
        httpResponse.setCookie("Path", "/");
        httpResponse.setCookie("max-age", 100);
        // ToDo
        // cookie max-age를 설정해주니까 지워졌다? Why?
        // cookie를 객체화할 필요성이 생겼다.
        return "redirect:/index.html";
    }

    @GetMapping(url = "/user/logout")
    public String logout(HttpResponse httpResponse) {
        httpResponse.setCookie("logined", false);
        httpResponse.setCookie("Path", "/");
        httpResponse.setCookie("max-age", 0);
        return "redirect:/index.html";
    }

}
