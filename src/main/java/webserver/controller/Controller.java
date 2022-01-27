package webserver.controller;

import util.request.HttpRequest;
import util.response.HttpResponse;

public interface Controller<T> {
    boolean supports(HttpRequest httpRequest);
    HttpResponse<T> handle(HttpRequest httpRequest);
}
