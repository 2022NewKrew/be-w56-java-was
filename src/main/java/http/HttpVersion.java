package http;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1"), HTTP_2("HTTP/2");

    private static final Map<String, HttpVersion> mappings =
        Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(HttpVersion::getValue, Function.identity())));
    private final String value;

    HttpVersion(String value) {
        this.value = value;
    }

    public static HttpVersion of(String value) {
        return mappings.get(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
