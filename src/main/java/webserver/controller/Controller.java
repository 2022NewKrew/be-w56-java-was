package webserver.controller;

import util.request.HttpRequest;
import util.response.HttpResponse;

import java.io.IOException;

public interface Controller<T> {
    boolean supports(HttpRequest httpRequest);
    HttpResponse<T> handle(HttpRequest httpRequest) throws IOException;
}
