package http;

import util.HttpRequestUtils.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.Constant.HEADER_FORMAT;
import static util.Constant.NEW_LINE;

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

    public String headers() {
        return headers.entrySet()
                .stream()
                .map(entry -> String.format(HEADER_FORMAT, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(NEW_LINE)) + NEW_LINE + NEW_LINE;
    }
}
