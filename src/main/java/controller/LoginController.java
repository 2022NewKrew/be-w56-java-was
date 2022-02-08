package controller;

import model.Request;
import model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;

import java.io.IOException;

public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String LOGIN_SUCCESS = "logined=true";
    private static final String LOGIN_FAIL = "logined=false";
    private static final String INDEX_URL = "/index.html";
    private static final String LOGIN_FAIL_URL = "/user/login_failed.html";
    private static final String COOKIE_USER_ID = "userId=";

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(Request request, Response response) throws IOException {

    }

    @Override
    protected void doPost(Request request, Response response) throws IOException {
        final String userId = request.getRequestBody("userId");
        final String password = request.getRequestBody("password");

        User user = userService.findUserByIdAndPassword(userId, password);

        if (user == null) {
            response.setCookie(LOGIN_FAIL);
            response.setCookie(COOKIE_USER_ID);
            response.set302Header(LOGIN_FAIL_URL);

            log.info("login fail!! => userId : {}, password : {}", userId, password);

            return;
        }

        response.setCookie(LOGIN_SUCCESS);
        response.setCookie(COOKIE_USER_ID + user.getUserId());
        response.set302Header(INDEX_URL);

        log.info("login success : {}", user);
    }
}
