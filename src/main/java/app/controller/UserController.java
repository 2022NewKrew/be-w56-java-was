package app.controller;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.annotation.Controller;
import app.annotation.GetMapping;
import app.annotation.PostMapping;
import app.db.DataBase;
import app.exception.UserNotFoundException;
import app.http.HttpResponse;
import app.model.user.User;
import webserver.template.Model;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/")
    public String index() {
        return "/index";
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
        return "redirect:/index";
    }

    @PostMapping(value = "/user/login")
    public String login(Map<String, String> body, HttpResponse httpResponse) {
        log.debug("UserController - login");
        String userId = body.get("userId");
        String password = body.get("password");
        User user = DataBase.findUserById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (!user.validatePassword(password)) {
            log.debug("로그인 실패, 비밀번호 불일치 {}", httpResponse.getCookie().cookieValue());
            return "redirect:/user/login_failed";
        }
        httpResponse.setCookie("logined", "true");
        log.debug("로그인 성공 {}", httpResponse.getCookie().cookieValue());
        return "redirect:/index";
    }

    @GetMapping(value = "/user")
    public String users(HttpResponse response, Model model) {
        Collection<User> users = DataBase.findAll();
        model.addAttribute("users", users);
        model.addAttribute("test", new User("testman","1234","testy","test@gmail.com"));
        return "/user/list";
    }
}
