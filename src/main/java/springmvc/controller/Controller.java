package springmvc.controller;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class Controller {

    public String doService(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            doGet(httpRequest, httpResponse);
        } else if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            doPost(httpRequest, httpResponse);
        }
        return "";
    }

    protected abstract String doGet(HttpRequest httpRequest, HttpResponse httpResponse);

    protected abstract String doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
