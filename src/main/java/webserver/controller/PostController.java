package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.manage.RequestParser;
import webserver.service.PostService;

import java.io.OutputStream;

public class PostController implements MethodController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String ADD_MEMO = "/memo/add";

    private static final String SIGN_UP = "/user/create";
    private static final String SIGN_IN = "/user/login";
    private static final String LOGOUT = "/user/logout";

    RequestParser rp;
    PostService ps;

    public PostController(RequestParser rp, OutputStream os) {
        this.rp = rp;
        this.ps = new PostService(os);
    }

    public void service() {
        log.info(":: Post Service");

        switch (rp.getPath()) {
            case ADD_MEMO:
                ps.methodAddMemo(rp);
                break;
            case SIGN_UP:
                ps.methodSignUp(rp);
                break;
            case SIGN_IN:
                ps.methodSignIn(rp);
                break;
            case LOGOUT:
                ps.methodLogout();
            default:
                break;
        }
    }
}
