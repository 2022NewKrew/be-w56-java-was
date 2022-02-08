package webserver.application.controller;

import webserver.application.model.User;
import webserver.application.service.UserService;
import webserver.framwork.core.Model;
import webserver.framwork.core.RequestMapping;
import webserver.framwork.http.request.HttpMethod;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.util.List;
import java.util.Map;

public class UserController {
    private static final UserController instance = new UserController();

    private final UserService userService = UserService.getInstance();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    @RequestMapping(value = "/index.html", method = HttpMethod.GET)
    public String index(HttpRequest request, HttpResponse response, Model model) {
        return "index";
    }

    @RequestMapping(value = "/user/create", method = HttpMethod.POST)
    public String join(HttpRequest request, HttpResponse response, Model model) {
        Map<String, String> body = request.getBody();
        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );

        userService.joinNewUser(user);

        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/login", method = HttpMethod.POST)
    public String login(HttpRequest request, HttpResponse response, Model model) {
        Map<String, String> body = request.getBody();

        if (userService.login(body.get("userId"), body.get("password"))) {
            response.setCookie("logined", "true");
            return "redirect:/index.html";
        }
        response.setStatus(HttpStatus.Unauthorized);
        response.setCookie("logined", "false");
        return "user/login_failed";
    }

    @RequestMapping(value = "/user/list.html", method = HttpMethod.GET)
    public String getUserList(HttpRequest request, HttpResponse response, Model model) {
        String loginCookie = request.getCookie("logined");
        if (!loginCookie.isEmpty() && loginCookie.equals("true")) {
            List<User> users = userService.getAllUser();
            model.addAttribute("users", users);
            return "user/list";
        }

        return "user/login";
    }
}
