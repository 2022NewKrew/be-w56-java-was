package webserver.handler.mappingValidator;

import app.http.HttpRequest;

import java.lang.reflect.Method;

public interface MappingValidator {
    boolean validateURL(HttpRequest request, Method method);
}
