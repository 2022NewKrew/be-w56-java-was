package http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    UNKNOWN("UNKNOWN");

    private String code;

    HttpMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private static final Map<String, HttpMethod> codeMap =
            Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(HttpMethod::getCode, Function.identity())));

    public static HttpMethod getHttpMethod(String code) {
        return Optional.ofNullable(codeMap.get(code)).orElseGet(() -> UNKNOWN);
    }
}
