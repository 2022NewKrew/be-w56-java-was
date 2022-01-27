package webserver.handler;

import app.http.HttpRequest;

public interface HandlerAdapter {
    boolean supports(Object handlerMethod);
    String handle(HttpRequest request, HandlerMethod handlerMethod);
}
