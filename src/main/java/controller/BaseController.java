package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.ModelAndView;

public interface BaseController {
    ModelAndView process(HttpRequest request, HttpResponse response);
}
