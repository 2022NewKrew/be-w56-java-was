package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.manage.RequestParser;
import webserver.service.GetService;

import java.io.*;

public class GetController implements MethodController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    RequestParser rp;
    GetService gs;


    private final String MEMO_LIST = "/index";

    private final String USER_LIST = "/user/list";

    private final String HTML_MEMO_LIST = "/index.html";
    private final String HTML_USER_LIST = "/user/list.html";

    public GetController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.gs = new GetService(os);
    }

    public void service() {
        log.info(":: GET Service");

        switch (rp.getPath()) {
            case MEMO_LIST:
            case HTML_MEMO_LIST:
                gs.methodMemoList(rp);
                break;
            case USER_LIST:
            case HTML_USER_LIST:
                gs.methodUserList(rp);
                break;
            default:
                gs.methodDefault(rp);
                break;
        }
    }
}
