package http;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpHeader {

    ACCEPT("Accept"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    HOST("Host"),
    CONNECTION("Connection"),
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie"),
    LOCATION("Location");
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

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
