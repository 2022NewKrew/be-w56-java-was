package util;

import exception.UnsupportedMethodException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE;

    private static final Map<String, RequestMethod> stringToRequestMethod;
    static {
        stringToRequestMethod = new HashMap<>();
        Arrays.stream(RequestMethod.values()).forEach(value -> stringToRequestMethod.put(value.name().toUpperCase(), value));
    }

    public static RequestMethod getRequestMethod(String requestMethodAsString) {
        RequestMethod requestMethod = stringToRequestMethod.get(requestMethodAsString.toUpperCase());
        if (requestMethod == null) {
            throw new UnsupportedMethodException();
        }
        return requestMethod;
    }

    @Override
    public String toString() {
        return "RequestMethod=" + this.name();
    }
}
