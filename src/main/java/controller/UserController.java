package controller;

import model.User;
import service.UserService;
import webserver.framwork.core.Model;
import webserver.framwork.core.RequestMapping;
import webserver.framwork.http.request.HttpMethod;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.util.Map;

public class UserController {
    private final UserService userService = UserService.getInstance();

//    private static UserController userController;
//
//    private UserController() {
//    }
//
//    public static UserController getInstance() {
//        if (userController == null) {
//            userController = new UserController();
//        }
//        return userController;
//    }


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

        return "redirect:/index";
//        httpResponseBuilder
//                .setStatus(HttpStatus.Redirect)
//                .addHeaderValue("Location", "/index.html")
//                .build();
    }

    @RequestMapping(value = "/user/login", method = HttpMethod.POST)
    public String login(HttpRequest request, HttpResponse response, Model model) {
        Map<String, String> body = request.getBody();

        if (userService.login(body.get("userId"), body.get("password"))) {
            response.setCookie("logined", "true");
            return "redirect:/index";
        }
        response.setStatus(HttpStatus.Unauthorized);
        response.setCookie("logined", "false");
        return "user/login_failed";
    }
}
