package http;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    private static final Map<String, HttpMethod> mappings =
        Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(HttpMethod::name, Function.identity())));

    public static HttpMethod of(String method) {
        if (Objects.isNull(method)) {
            return null;
        }
        return mappings.get(method);
    }
}
