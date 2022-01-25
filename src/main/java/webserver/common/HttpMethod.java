package webserver.common;

import java.util.Locale;

public enum HttpMethod {
    GET, POST, UNKNOWN;

    public static HttpMethod findMethod(String name) {
        try {
            return HttpMethod.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return HttpMethod.UNKNOWN;
        }
    }
}
