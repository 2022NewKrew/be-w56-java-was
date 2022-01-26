package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.IOException;
import java.util.Map;

public class FrontController {

    public static void getResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String view;

        if (httpRequest.getPath().equals("/user/create")) {
            Map<String, String> query = httpRequest.getQueryString();
            view = AuthController.signUp(query.get("userId"), query.get("password"), query.get("name"), query.get("email"));

        } else {
            view = httpRequest.getPath();
        }

        httpResponse.setStatus(HttpStatus.OK);
        httpResponse.setBody(ViewMapper.getBytes(view));
    }
}
