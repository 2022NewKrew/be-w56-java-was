package webserver.http.Packet;

import java.util.stream.Stream;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static HttpMethod parse(String method) {
        return Stream.of(values())
                .filter(m -> m.method.equals(method))
                .findFirst().orElseThrow();
    }
}
