package controller;

import annotation.Controller;
import annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.PathInfo;

import java.io.IOException;
import java.sql.SQLException;

@Controller(value = "/user")
public class UserController{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    @RequestMapping(value = PathInfo.PATH_USER_CREATE_REQUEST, requestMethod = "POST")
    public HttpResponse createUser(HttpRequest httpRequest) {
        try {
            userService.store(httpRequest);
            return ResponseGenerator.generateResponse302(PathInfo.PATH_USER_LIST);
        } catch (Exception e) {
            return ResponseGenerator.generateResponse400(e);
        }
    }

    @RequestMapping(value = PathInfo.PATH_LOGIN_REQUEST, requestMethod = "POST")
    public HttpResponse tryLogin(HttpRequest httpRequest) {
        boolean canLogin = userService.canLogIn(httpRequest);
        if (canLogin) {
            log.debug("Login success!");
            return ResponseGenerator.generateLoginResponse();
        }
        log.debug("Login failed");
        return ResponseGenerator.generateLoginFailedResponse();
    }

    @RequestMapping(value = PathInfo.PATH_USER_LIST, requestMethod = "GET")
    public HttpResponse getUserList(HttpRequest httpRequest) {
        log.debug("loggedin: {}", httpRequest.isLoggedIn());
        if (httpRequest.isLoggedIn()) {
            try {
                return ResponseGenerator.generateUserListResponse(userService.findAll());
            } catch (SQLException e) {
                log.error("SQL Exception occured");
                return ResponseGenerator.generateResponse500();
            } catch (IOException e) {
                log.error("IO Exception occured");
                return ResponseGenerator.generateResponse500();
            }
        }
        return ResponseGenerator.generateResponse302(PathInfo.PATH_LOGIN_PAGE);
    }
}