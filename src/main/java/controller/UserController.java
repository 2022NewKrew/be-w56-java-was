package controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import db.DataBase;
import exception.InvalidRequestException;
import exception.TemplateProcessingException;
import java.util.Collection;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseFactory;
import webserver.response.TemplateResponse.Model;

@Controller
public final class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String HOME_URL = "/index.html";

    @PostMapping("/user/create")
    public Response userAdd(Request request) throws InvalidRequestException {
        log.debug("user {} 생성 시도", request.getParam("userId"));
        if (DataBase.findUserById(request.getParam("userId")) != null) {
            throw new InvalidRequestException("UserId 중복");
        }
        DataBase.addUser(User.builder()
            .userId(request.getParam("userId"))
            .password(request.getParam("password"))
            .name(request.getParam("name"))
            .email(request.getParam("email"))
            .build());
        return ResponseFactory.redirect(HOME_URL);
    }

    @PostMapping("/user/login")
    public Response userLogin(Request request) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        log.debug("user login 시도: {}, {}", userId, password);

        if (DataBase.findUserById(userId) == null ||
            !DataBase.findUserById(userId).getPassword().equals(password)) {
            return ResponseFactory.redirect("/user/login_failed.html")
                .setCookie("logined=false");
        }
        return ResponseFactory.redirect(HOME_URL)
            .setCookie("logined=true&userId=" + request.getParam("userId"), "Path=/");
    }

    @GetMapping("/user/list")
    public Response userList(Request request, Model model) throws TemplateProcessingException {
        if (validateUser(request)) {
            Collection<User> users = DataBase.findAll();
            log.debug("model: " + model);
            log.debug(String.valueOf(users.size()));
            model.addAttribute("users", users);
            return ResponseFactory.template("user/list");
        }
        return ResponseFactory.template("user/list");
    }

    private boolean validateUser(Request request) {
        //String logedIn = request.getCookieValue("login");
        //return logedIn != null && logedIn.equals("true");
        return true;
    }
}
