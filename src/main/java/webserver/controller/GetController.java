package webserver.controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.manage.ResponseCode;
import webserver.manage.ResponseFile;
import webserver.RequestHandler;
import webserver.manage.RequestParser;
import webserver.manage.ResponseFormat;

import java.io.*;

public class GetController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String SIGN_UP = "/user/create";

    RequestParser rp;
    OutputStream os;

    public GetController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() {
        log.info(":: GET Service");

        switch (rp.getPath()) {
            case SIGN_UP:
                methodSignUp();
                break;
            default:
                methodDefault();
                break;
        }
    }

    private void methodSignUp () {
        String userId = rp.getQuery("userId");
        String password = rp.getQuery("password");
        String name = rp.getQuery("name");
        String email = rp.getQuery("email");
        User user = new User(userId,password,name,email);

        log.info(user.toString());
        try {
            ResponseFormat rf = new ResponseFormat(os, ResponseFile.ERROR_FILE);
            rf.sendResponse(ResponseCode.STATUS_200);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void methodDefault () {
        try {
            ResponseFormat rf = new ResponseFormat(os, rp.getPath());
            rf.sendResponse(ResponseCode.STATUS_200);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
