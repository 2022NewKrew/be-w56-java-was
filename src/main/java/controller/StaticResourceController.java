package controller;

import webserver.model.HttpRequest;

public class StaticResourceController implements BaseController {

    @Override
    public String process(HttpRequest request) {
        return request.getUrl();
    }

}
