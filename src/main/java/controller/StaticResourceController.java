package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public class StaticResourceController implements BaseController {

    @Override
    public HttpResponse process(HttpRequest request) {
        return new HttpResponse(request.getUrl());
    }

}
