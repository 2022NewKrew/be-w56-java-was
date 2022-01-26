package webserver.requesthandler.httprequest;

import java.util.Objects;
import java.util.stream.Stream;

import static webserver.common.exception.ExceptionMessage.UNSUPPORTED_HTTP_METHOD_EXCEPTION;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static Stream<HttpMethod> stream() {
        return Stream.of(HttpMethod.values());
    }

    public static HttpMethod of(String method) throws UnsupportedOperationException {
        return HttpMethod.stream()
                .filter(httpMethod -> httpMethod.equals(method))
                .findFirst()
                .orElseThrow(() -> {
                    throw new UnsupportedOperationException(UNSUPPORTED_HTTP_METHOD_EXCEPTION.getMessage());
                });
    }

    public boolean equals(String method) {
        return Objects.equals(this.method, method);
    }
}
