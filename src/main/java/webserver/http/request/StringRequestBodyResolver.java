package webserver.http.request;

import http.HttpRequest;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class StringRequestBodyResolver implements RequestBodyResolver {
    @Override
    public boolean supports(Type type) {
        return type.equals(StringBody.class);
    }

    @Override
    public StringBody resolveRequestBody(HttpRequest httpRequest, Type type) {
        byte[] requestBody = httpRequest.getBody();
        return new StringBody(new String(requestBody, StandardCharsets.UTF_8));
    }
}
