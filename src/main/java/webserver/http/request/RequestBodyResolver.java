package webserver.http.request;

import http.HttpRequest;

import java.lang.reflect.Type;

public interface RequestBodyResolver {
    boolean supports(Type type);
    RequestBody<?> resolveRequestBody(HttpRequest httpRequest, Type type);
}
