package webserver;

import http.HttpRequest;
import http.HttpResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    HttpResponse handle(HttpRequest request, Object handler);
}
