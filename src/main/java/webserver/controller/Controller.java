package webserver.controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Controller {
    HttpResponse service(HttpRequest request);
}
