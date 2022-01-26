package webserver;

import http.HttpRequest;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Class<?> clazz, HttpRequest httpRequest);

    Object resolveArgument(Object instance, HttpRequest httpRequest) throws Exception;
}
