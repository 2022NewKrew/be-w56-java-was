package controller;

import webserver.HttpRequest;

public class StaticController implements BaseController {

    @Override
    public String process(HttpRequest request) {
        return request.getUrl();
    }

}
