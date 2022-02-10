package webserver;

import http.HttpRequest;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(HttpRequest request, Object handler);
}
