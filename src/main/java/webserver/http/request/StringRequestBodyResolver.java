package webserver.http.request;

import http.ContentType;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class StringRequestBodyResolver implements RequestBodyResolver {

    @Override
    public boolean supports(ContentType contentType, Type type) {
        return type.equals(String.class);
    }

    @Override
    public Object resolveRequestBody(byte[] requestBody, Type type) {
        return new String(requestBody, StandardCharsets.UTF_8);
    }
}
