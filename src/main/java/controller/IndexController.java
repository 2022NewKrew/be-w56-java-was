package controller;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.ViewService;

import java.io.IOException;

public class IndexController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Override
    public void makeResponse(Request request, Response response) throws IOException {
        boolean checkLogin = UserService.checkLogin(request);
        log.debug("checkLogin: {}", checkLogin);

        byte[] body = ViewService.getView("/index.html");
        response.staticResponse(body);

    }
}
