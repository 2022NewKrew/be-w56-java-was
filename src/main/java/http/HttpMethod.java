package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final Map<String, HttpMethod> mappings;

    static {
        mappings = new HashMap<>(16);
        Arrays.stream(values()).forEach(method -> mappings.put(method.name(), method));
    }

    public static HttpMethod resolve(String method) {
        if (Objects.isNull(method)) {
            return null;
        }
        return mappings.get(method);
    }
}
