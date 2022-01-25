package http;

import javax.annotation.Nullable;

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

    @Nullable
    public static Method fromString(String method) {
        if (method == null) {
            return null;
        }
        for (Method m : Method.values()) {
            if (m.name().equalsIgnoreCase(method)) {
                return m;
            }
        }
        return null;
    }
}
