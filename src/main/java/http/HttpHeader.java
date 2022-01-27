package http;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeader {

    private static final String HEADER_FORMAT = "%s: %s";

    private final Map<String, String> header;

    public static HttpHeader of(Map<String, String> header) {
        return new HttpHeader(header);
    }

    private HttpHeader(Map<String, String> header) {
        this.header = header;
    }

    public String get(String key) {
        return header.getOrDefault(key, "");
    }

    public List<String> toFormattedStrings() {
        return header.entrySet().stream()
            .map(entry -> String.format(HEADER_FORMAT, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }
}
