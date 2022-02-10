package webserver.http.message.values;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpMethod {
    GET("GET"),
    POST("POST")
    ;

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    private static final Map<String, HttpMethod> methods = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(HttpMethod::getMethod, Function.identity())));

    public static HttpMethod find(String method) {
        return methods.get(method);
    }

    public String getMethod() {
        return method;
    }
}
