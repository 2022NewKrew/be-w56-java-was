package mvcframework;

import webserver.HttpRequest;

public interface HandlerMapping {
    Object getHandler(HttpRequest request);
}
