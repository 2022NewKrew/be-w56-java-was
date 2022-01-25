package controller;

import webserver.HttpRequest;
import webserver.ViewMapper;

import java.io.IOException;
import java.util.Map;

public class FrontController {

    public static byte[] getView(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getPath().equals("/user/create")) {
            Map<String, String> query = httpRequest.getQueryString();
            String view = AuthController.signUp(query.get("userId"), query.get("password"), query.get("name"), query.get("email"));
            return ViewMapper.getBytes(view);
        }
        return ViewMapper.getBytes(httpRequest.getPath());
    }
}
