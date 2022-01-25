package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.manage.RequestParser;

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
        User user = new User(userId,password,name,email);

        log.info(user.toString());

    }
}
