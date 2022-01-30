package web.controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.dto.LoginDto;

import java.util.Optional;

@Controller
public class UserController {
    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @PostMapping("/user/create")
    public String createUsers(User user) {
        log.info("/users/create : {}", user.toString());
        DataBase.addUser(user);
        return "redirect:/";
    }

    @PostMapping("/user/login")
    public String login(LoginDto loginDto) {
        Optional<User> user = DataBase.findUserById(loginDto.getUserId());
        if (user.isPresent() && user.get().getPassword().equals(loginDto.getPassword())) {
            log.info("/login : 성공");
            return "redirect:/";
        }
        log.info("/login : 실패");
        return "redirect:/user/login.html";
    }
}
