package webserver.http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpStatus {
    OK(200, "200 OK"),
    FOUND(302, "302 FOUND");

    private final int code;
    private final String description;
    private static final Map<Integer, HttpStatus> statusMap = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(HttpStatus::getCode, Function.identity())));

    HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HttpStatus match(int statusCode) {
        return Optional.ofNullable(statusMap.get(statusCode))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HttpMethod"));
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
