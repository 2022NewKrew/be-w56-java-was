package webserver.http.request;

import http.ContentType;

import java.lang.reflect.Type;

public class ByteArrayRequestBodyResolver implements RequestBodyResolver{
    @Override
    public boolean supports(ContentType contentType, Type type) {
        return type.equals(byte[].class);
    }

    @Override
    public Object resolveRequestBody(byte[] requestBody, Type type) {
        return requestBody;
    }
}
