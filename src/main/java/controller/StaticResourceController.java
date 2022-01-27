package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.ModelAndView;

public class StaticResourceController implements BaseController {

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        return new ModelAndView(request.getUrl());
    }

}
