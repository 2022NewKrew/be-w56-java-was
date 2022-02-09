package webserver.service;

import db.DataBase;
import webserver.manage.RequestParser;
import webserver.page.PageOfMemoList;
import webserver.page.PageOfUserList;
import webserver.response.ResponseCode;
import webserver.response.format.ForwardResponseFormat;
import webserver.response.format.ResponseFormat;

import java.io.OutputStream;

public class GetService {
    private final String LOGIN_KEY = "logined";
    private final String LOGIN_PAGE = "/user/login.html";

    OutputStream os;

    public GetService(OutputStream os) {
        this.os = os;
    }

    public void methodMemoList() {
        String page = PageOfMemoList.draw(DataBase.findMemoAll());

        ForwardResponseFormat rf = new ForwardResponseFormat(os);
        rf.setHtmlPage(page);
        rf.sendResponse(ResponseCode.STATUS_200);
    }

    public void methodUserList(RequestParser rp) {
        String logined = rp.getCookie(LOGIN_KEY);
        if( logined == null || logined.equalsIgnoreCase("") ) {
            ResponseFormat rf = new ForwardResponseFormat(os, LOGIN_PAGE);
            rf.sendResponse(ResponseCode.STATUS_200);
            return;
        }

        String page = PageOfUserList.draw(DataBase.findUserAll());

        ForwardResponseFormat rf = new ForwardResponseFormat(os);
        rf.setHtmlPage(page);
        rf.sendResponse(ResponseCode.STATUS_200);
    }

    public void methodDefault (RequestParser rp) {
        ResponseFormat rf = new ForwardResponseFormat(os, rp.getPath());
        rf.sendResponse(ResponseCode.STATUS_200);
    }
}
