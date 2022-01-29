package web.controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @GetMapping("/user/create")
    public String createUsers(User user) {
        log.info("/users/create : {}", user.toString());
        return "/index.html";
    }

    @PostMapping("/user/join")
    public String join() {
        return "/create/index.html";
    }
}
