package controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.model.ModelAndView;

public interface BaseController {
    ModelAndView process(HttpRequest request, HttpResponse response);
}
