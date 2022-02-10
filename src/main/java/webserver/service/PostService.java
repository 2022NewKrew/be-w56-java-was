package webserver.service;

import db.DataBase;
import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.manage.RequestParser;
import webserver.response.ResponseCode;
import webserver.response.format.RedirectResponseFormat;
import webserver.response.format.ResponseFormat;

import java.io.OutputStream;

public class PostService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private OutputStream os;

    public PostService(OutputStream os) {
        this.os = os;
    }

    public void methodAddMemo (RequestParser rp) {
        log.info("[run] methodAddMemo");

        String writer = rp.getCookie("logined");
        String memo = rp.getBody("memo");

        ResponseFormat rf = new RedirectResponseFormat(os, "/");
        try {
            Memo newMemo = new Memo(writer, memo);
            log.info("new Memo :::" + newMemo);
            DataBase.addMemo(newMemo);

            rf.sendResponse(ResponseCode.STATUS_303);
            return;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        rf.sendResponse(ResponseCode.STATUS_404);
    }

    public void methodSignUp(RequestParser rp) {
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

    public void methodSignIn(RequestParser rp) {
        log.info("[run] methodSignIn");

        String userId = rp.getBody("userId");
        String password = rp.getBody("password");

        User userData = DataBase.findUserById(userId);
        if(userData != null && userData.getPassword().equals(password)) {
            log.info("[login] success");

            ResponseFormat rf = new RedirectResponseFormat(os, "/");
            rf.setCookie("logined", userId);
            rf.sendResponse(ResponseCode.STATUS_303);
            return;
        }
        ResponseFormat rf = new RedirectResponseFormat(os, "/");
        rf.setCookie("logined", "");
        rf.sendResponse(ResponseCode.STATUS_404);
    }

    public void methodLogout() {
        log.info("[run] methodLogout");
        ResponseFormat rf = new RedirectResponseFormat(os, "/");
        rf.setCookie("logined", "");
        rf.sendResponse(ResponseCode.STATUS_303);
    }
}
