package webserver;

import http.HttpRequest;

public interface HandlerMapping {

    Object getHandler(HttpRequest request);
}
