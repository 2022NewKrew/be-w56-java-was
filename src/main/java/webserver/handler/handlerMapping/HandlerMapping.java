package webserver.handler.handlerMapping;

import app.http.HttpRequest;
import webserver.handler.HandlerMethod;

public interface HandlerMapping {
    HandlerMethod getHandlerMethod(HttpRequest request);
}
