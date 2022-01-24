package util;

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

    public static RequestMethod getRequestMethod(String requestMethod) {
        // TODO Define NoSuchMethod Exception
        return stringToRequestMethod.get(requestMethod.toUpperCase());
    }

    @Override
    public String toString() {
        return "RequestMethod=" + this.name();
    }
}
