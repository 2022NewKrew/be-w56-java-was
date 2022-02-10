package webserver.http.message.values;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpHeaderNames {
    ACCEPT("Accept"),
    COOKIE("Cookie"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    ;

    private final String name;

    HttpHeaderNames(String name) {
        this.name = name;
    }

    private static final Map<String, HttpHeaderNames> names = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(HttpHeaderNames::getName, Function.identity())));

    public static HttpHeaderNames find(String name) {
        return names.get(name);
    }

    public String getName() {
        return name;
    }
}
