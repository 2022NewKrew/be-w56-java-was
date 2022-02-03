package http;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpHeader {

    ACCEPT("Accept"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    HOST("Host"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie");

    private static final Map<String, HttpHeader> mappings = Collections.unmodifiableMap(
        Stream.of(values()).collect(
            Collectors.toMap(HttpHeader::getValue, Function.identity())));
    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    public static HttpHeader of(String value) {
        return mappings.get(value);
    }

    public static boolean contains(String key) {
        return mappings.containsKey(key);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
