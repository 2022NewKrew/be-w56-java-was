package webserver.http.request;

import http.HttpRequest;

import java.lang.reflect.Type;

public class EmptyBodyResolver implements RequestBodyResolver {
    @Override
    public boolean supports(Type type) {
        return type.equals(EmptyBody.class);
    }

    @Override
    public EmptyBody resolveRequestBody(HttpRequest httpRequest, Type type) {
        return new EmptyBody();
    }
}
