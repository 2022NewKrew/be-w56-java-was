package webserver.handler.handlerAdapter;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.handler.HandlerMethod;

public interface HandlerAdapter {
    boolean supports(Object handlerMethod);
    void handle(HttpRequest request, HttpResponse response, HandlerMethod handlerMethod);
}
