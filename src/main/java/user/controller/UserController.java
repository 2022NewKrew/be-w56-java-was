package user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import user.domain.User;
import user.dto.request.LoginRequest;
import user.dto.request.SignUpRequest;
import user.service.UserService;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.domain.HttpStatus;
import webserver.domain.Request;
import webserver.domain.Response;

@Controller
public class UserController {

    private static final String LOGIN_SUCCESS_COOKIE = "logined=true; Path=/";
    private static final String LOGIN_FAIL_COOKIE = "logined=false; Path=/";

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/create", method = "POST")
    public Response signUpUser(Request request) {
        String userId = request.getBodyAttribute("userId");
        String password = request.getBodyAttribute("password");
        String name = request.getBodyAttribute("name");
        String email = request.getBodyAttribute("email");
        SignUpRequest signUpRequest = new SignUpRequest(userId, password, name, email);

        User user = signUpRequest.toUser();
        userService.save(user);

        logger.info("회원 가입: {}", userId);

        return Response.createResponse(HttpStatus.FOUND, "/index.html");
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public Response login(Request request) {
        String userId = request.getBodyAttribute("userId");
        String password = request.getBodyAttribute("password");
        LoginRequest loginRequest = new LoginRequest(userId, password);

        User user = loginRequest.toUser();
        boolean isLogin = userService.login(user);

        logger.info("로그인: {}", userId);

        String location = isLogin ? "/index.html" : "/user/login_failed.html";
        String cookie = isLogin ? LOGIN_SUCCESS_COOKIE : LOGIN_FAIL_COOKIE;
        return Response.createResponse(HttpStatus.FOUND, location).setCookie(cookie);
    }
}
