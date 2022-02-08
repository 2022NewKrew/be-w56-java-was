package lib.was.http;

import java.util.Optional;

public enum Method {
    GET,
    POST,
    PATCH,
    PUT,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT;

    public static Optional<Method> fromString(String method) {
        if (method == null) {
            return Optional.empty();
        }
        for (Method m : Method.values()) {
            if (m.name().equalsIgnoreCase(method)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }
}
