package webserver.controller;

import annotation.GetMapping;
import annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import model.*;
import webserver.Model;
import webserver.service.UserService;
import webserver.web.request.Request;
import webserver.web.response.Response;

import java.util.List;

@Slf4j
@annotation.Controller
public class UserController extends BaseController {

    private final UserService userService = UserService.getInstance();
    private static final UserController userController = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return userController;
    }

    @PostMapping(url = "/user/create")
    public String postJoinUser(String userId, String name, String password, String email) {
        User user = setUserInformation(userId, name, password, email);
        userService.joinUser(user);
        return "redirect:/index.html";
    }

    @PostMapping(url = "/user/login")
    public String loginUser(String userId, String password, Request request, Response.ResponseBuilder builder) {
        if (request.inquireHeaderData("Cookie").contains("logined=true")) {
            log.info("이미 로그인된 유저");
            return "redirect:/index.html";
        }
        if (userService.loginUser(userId, password)) {
            builder.setCookie("logined=true; Path=/");
            log.info("로그인 성공");
            return "redirect:/index.html";
        }
        builder.setCookie("logined=false");
        log.info("로그인 실패");
        return "redirect:/user/login_failed.html";
    }

    @GetMapping(url = "/user/list")
    public String showUserList(Request request, Model model) {
        if (!request.inquireHeaderData("Cookie").contains("logined=true")) {
            return "redirect:/user/login";
        }
        List<User> users = userService.getAllUsers();
        model.addAllAttribute("users", users);
        return "user/list.html";
    }

    private User setUserInformation(String userId, String name, String password, String email) {
        UserId joinUserId = new UserId(userId);
        Name userName = new Name(name);
        Password userPassword = new Password(password);
        Email userEmail = new Email(email);
        return new User(joinUserId, userPassword, userName, userEmail);
    }
}
