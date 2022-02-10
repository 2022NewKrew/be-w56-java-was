package springmvc.controller;

import springmvc.ModelAndView;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class Controller {

    public ModelAndView doService(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            return doGet(httpRequest, httpResponse);
        } else if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            return doPost(httpRequest, httpResponse);
        } else if (httpRequest.getMethod().equals(HttpMethod.PUT)) {
            return doPut(httpRequest, httpResponse);
        }
        return new ModelAndView();
    }

    private ModelAndView doPut(HttpRequest httpRequest, HttpResponse httpResponse) {
        return null;
    }

    protected ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        return null;
    };

    protected ModelAndView doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        return null;
    };
}
