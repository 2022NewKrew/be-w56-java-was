package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.manage.RequestParser;
import webserver.response.format.ForwardResponseFormat;
import webserver.response.format.ResponseFormat;
import webserver.response.ResponseCode;

import java.io.*;

public class GetController implements MethodController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    RequestParser rp;
    OutputStream os;

    private final String LOGIN_KEY = "logined";
    private final String USER_LIST = "/user/list";
    private final String LOGIN_PAGE = "/user/login.html";

    public GetController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.os = os;
    }

    public void service() {
        log.info(":: GET Service");

        switch (rp.getPath()) {
            case USER_LIST:
                methodUserList();
                break;
            default:
                methodDefault();
                break;
        }
    }

    private void methodUserList() {
        String logined = rp.getCookie(LOGIN_KEY);
        if( logined == null || logined.equalsIgnoreCase("false") ) {
            ResponseFormat rf = new ForwardResponseFormat(os, LOGIN_PAGE);
            rf.sendResponse(ResponseCode.STATUS_200);
            return;
        }
        ResponseFormat rf = new ForwardResponseFormat(os, USER_LIST+".html");
        rf.sendResponse(ResponseCode.STATUS_200);
    }

    private void methodDefault () {
        ResponseFormat rf = new ForwardResponseFormat(os, rp.getPath());
        rf.sendResponse(ResponseCode.STATUS_200);
    }
}
