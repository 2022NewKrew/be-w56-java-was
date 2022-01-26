package webserver;

import http.HttpRequest;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Class<?> methodParameter, HttpRequest httpRequest);

    Object resolverArgument(Object instance, HttpRequest httpRequest);
}
