package app.http;

import static util.Constant.HEADER_FORMAT;
import static util.Constant.NEW_LINE;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.HttpRequestUtils.Pair;

public class HttpHeader {
    private final Map<String, String> headers;

    public static HttpHeader of(List<Pair> pairs) {
        return new HttpHeader(pairs);
    }

    private HttpHeader(List<Pair> pairs) {
        headers = pairs.stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public String get(String key) {
        return headers.getOrDefault(key, "");
    }

    public String get(String key, String defaultValue) {
        return headers.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        headers.put(key, value);
    }

    public String headers() {
        return headers.entrySet()
                .stream()
                .map(entry -> String.format(HEADER_FORMAT, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(NEW_LINE)) + NEW_LINE + NEW_LINE;
    }
}
