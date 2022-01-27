package webserver.handler;

import app.http.HttpRequest;

public interface HandlerMapping {
    HandlerMethod getHandlerMethod(HttpRequest request);
}
