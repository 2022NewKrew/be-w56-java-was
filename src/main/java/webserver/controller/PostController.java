package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.manage.RequestParser;
import webserver.response.PostResponseFormat;
import webserver.response.ResponseCode;
import webserver.response.ResponseFormat;

import java.io.IOException;
import java.io.OutputStream;

public class PostController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String SIGN_UP = "/user/create";

    RequestParser rp;
    OutputStream os;

    public PostController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() throws IOException {
        log.info(":: Post Service");

        switch (rp.getPath()) {
            case SIGN_UP:
                methodSignUp();
                break;
            default:
                break;
        }
    }

    private void methodSignUp() {
        String userId = rp.getBody("userId");
        String password = rp.getBody("password");
        String name = rp.getBody("name");
        String email = rp.getBody("email");

        ResponseFormat rf = new PostResponseFormat(os, "/");

        try {
            User user = new User(userId, password, name, email);
            log.info(user.toString());
            DataBase.addUser(user);

            rf.sendResponse(ResponseCode.STATUS_302);
            return;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        rf.sendResponse(ResponseCode.STATUS_405);
    }
}
