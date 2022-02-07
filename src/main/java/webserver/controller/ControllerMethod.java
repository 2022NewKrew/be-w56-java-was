package webserver.controller;

import webserver.http.response.HttpResponse;
import webserver.http.request.HttpRequest;

@FunctionalInterface
public interface ControllerMethod {
    void run(HttpRequest req, HttpResponse res);
}
