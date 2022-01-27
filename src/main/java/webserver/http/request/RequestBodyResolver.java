package webserver.http.request;

import http.ContentType;

import java.lang.reflect.Type;

public interface RequestBodyResolver {
    boolean supports(ContentType contentType, Type type);
    Object resolveRequestBody(byte[] requestBody, Type type);
}
