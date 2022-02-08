package controller;

import dto.UserLoginRequest;
import dto.UserSignupRequest;
import exception.UserNotFoundException;
import http.HttpHeader;
import http.HttpResponse;
import http.PostMapping;
import service.UserService;

@Controller
public class UserController {

    private final UserService userService = new UserService();

    @PostMapping("/user")
    public void signup(UserSignupRequest request, HttpResponse httpResponse) {
        userService.signup(request);
        httpResponse.sendRedirect("/index.html");
    }

    @PostMapping("/user/login")
    public void login(UserLoginRequest request, HttpResponse httpResponse) {
        try {
            userService.login(request);
            httpResponse.putHeader(HttpHeader.SET_COOKIE, "logined=true; Path=/");
            httpResponse.sendRedirect("/index.html");
        } catch (UserNotFoundException e) {
            httpResponse.putHeader(HttpHeader.SET_COOKIE, "logined=false; Path=/");
            httpResponse.sendRedirect("/user/login_failed.html");
        }
    }
}
