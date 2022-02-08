package controller;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.ViewService;

import java.io.IOException;

public class UserListController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @Override
    public void makeResponse(Request request, Response response) throws IOException {
        boolean checkLogin = UserService.checkLogin(request);
        log.debug("checkLogin: {}", checkLogin);

        if (checkLogin) {
            byte[] body = ViewService.getUserListBody("/user/list.html");
            response.staticResponse(body);
        } else {
            response.redirectResponse("/user/login.html");
        }
    }
}
