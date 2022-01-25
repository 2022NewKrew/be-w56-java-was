package webserver.header;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpMethod {
    POST,
    GET,
    PUT,
    PATCH,
    DELETE;

    private static final Map<String, HttpMethod> methodMap = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(HttpMethod::name, Function.identity())));

    public static HttpMethod match(String method) {
        return Optional.ofNullable(methodMap.get(method))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HttpMethod"));
    }
}
