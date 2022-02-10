package controller;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.Map;

public class LoginController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void makeResponse(Request request, Response response) {
        Map<String, String> loginInfo = request.getBody();
        boolean isLogin = UserService.login(loginInfo.get("userId"), loginInfo.get("password"));
        log.debug("isLogin: {}", isLogin);
        if (isLogin) {
            response.redirectResponse("/", "logined=true;");
        } else {
            response.redirectResponse("/user/login_failed.html", "logined=false;");
        }
    }
}
