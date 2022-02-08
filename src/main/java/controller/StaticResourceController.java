package controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.model.ModelAndView;

public class StaticResourceController implements BaseController {

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        return new ModelAndView(request.getUrl());
    }

}
