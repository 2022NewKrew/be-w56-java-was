package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpHeaders {
    private final Map<String, String> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public int getContentLength() {
        return Integer.parseInt(get("Content-Length").orElse("0"));
    }

    public String join(String delimiter) {
        return headers.entrySet()
                .stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(delimiter));
    }

}
