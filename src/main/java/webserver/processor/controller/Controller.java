package webserver.processor.controller;

import http.HttpRequest;
import http.HttpResponse;

public interface Controller {
    boolean supports(HttpRequest httpRequest);
    HttpResponse process(HttpRequest httpRequest);
}
