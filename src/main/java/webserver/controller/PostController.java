package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.manage.RequestParser;
import webserver.response.format.RedirectResponseFormat;
import webserver.response.ResponseCode;
import webserver.response.format.ResponseFormat;

import java.io.OutputStream;

public class PostController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String SIGN_UP = "/user/create";
    private static final String SIGN_IN = "/user/login";
    private static final String LOGOUT = "/user/logout";

    RequestParser rp;
    OutputStream os;

    public PostController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() {
        log.info(":: Post Service");

        switch (rp.getPath()) {
            case SIGN_UP:
                methodSignUp();
                break;
            case SIGN_IN:
                methodSignIn();
                break;
            case LOGOUT:
                methodLogout();
            default:
                break;
        }
    }

    private void methodSignUp() {
        log.info("[run] methodSignUp");

        String userId = rp.getBody("userId");
        String password = rp.getBody("password");
        String name = rp.getBody("name");
        String email = rp.getBody("email");

        ResponseFormat rf = new RedirectResponseFormat(os, "/");

        try {
            User user = new User(userId, password, name, email);
            log.info(user.toString());
            DataBase.addUser(user);

            rf.sendResponse(ResponseCode.STATUS_303);
            return;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        rf.sendResponse(ResponseCode.STATUS_404);
    }

    private void methodSignIn() {
        log.info("[run] methodSignIn");

        String userId = rp.getBody("userId");
        String password = rp.getBody("password");

        User userData = DataBase.findUserById(userId);
        if(userData != null && userData.getPassword().equals(password)) {
            log.info("[login] success");

            ResponseFormat rf = new RedirectResponseFormat(os, "/");
            rf.setCookie("logined", "true");
            rf.sendResponse(ResponseCode.STATUS_303);
            return;
        }
        ResponseFormat rf = new RedirectResponseFormat(os, "/");
        rf.setCookie("logined", "false");
        rf.sendResponse(ResponseCode.STATUS_404);
    }

    private void methodLogout() {
        log.info("[run] methodLogout");
        ResponseFormat rf = new RedirectResponseFormat(os, "/");
        rf.setCookie("logined", "false");
        rf.sendResponse(ResponseCode.STATUS_303);
    }
}
