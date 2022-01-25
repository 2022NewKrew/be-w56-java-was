package util;

import exception.UnsupportedHttpMethodException;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ToString
public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    private static final Map<String, HttpMethod> STRING_HTTP_METHOD_MAP;
    static {
        STRING_HTTP_METHOD_MAP = new HashMap<>();
        Arrays.stream(HttpMethod.values()).forEach(value -> STRING_HTTP_METHOD_MAP.put(value.name().toUpperCase(), value));
    }

    public static HttpMethod getHttpMethod(String httpMethodAsString) {
        HttpMethod httpMethod = STRING_HTTP_METHOD_MAP.get(httpMethodAsString.toUpperCase());
        if (httpMethod == null) {
            throw new UnsupportedHttpMethodException();
        }
        return httpMethod;
    }
}
