package controller;

import http.Request;
import http.Response;

public class DefaultController implements Controller {
    public DefaultController() {}

    public String run(Request request, Response response) {
        return request.getRequestUrl();
    }
}
