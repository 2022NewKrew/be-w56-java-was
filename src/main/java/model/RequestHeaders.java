package model;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestHeaders {
    private static final String ACCEPT = "Accept";

    private final Map<String, String> requestHeaders;

    private RequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = new HashMap<>(requestHeaders);
    }

    public static RequestHeaders from(List<HttpRequestUtils.Pair> headerPairs) {
        Map<String, String> requestHeaders = headerPairs.stream()
                .collect(Collectors.toMap(headerPair -> headerPair.getKey(), headerPair -> headerPair.getValue()));

        return new RequestHeaders(requestHeaders);
    }

    public String getAcceptType(String path) {
        if (requestHeaders.containsKey(ACCEPT)) {
            String value = requestHeaders.getOrDefault(ACCEPT, "");
            return findAcceptType(value, path);
        }

        return "text/html";
    }

    private String findAcceptType(String value, String path) {
        if (value.contains("text/css") || path.endsWith(".css")) {
            return "text/css";
        }

        if (value.contains("javascript") || path.endsWith(".js")) {
            return "application/javascript";
        }

        if (path.endsWith(".ico")) {
            return "image/x-icon";
        }

        if (path.endsWith(".ttf")) {
            return "application/x-font-ttf";
        }
        if (path.endsWith(".woff")) {
            return "application/x-font-woff";
        }

        return "text/html";
    }

    public String getHeader(String key) {
        return requestHeaders.get(key);
    }

    public boolean isContainKey(String key) {
        return requestHeaders.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String key : requestHeaders.keySet()) {
            sb.append(key).append(" : ").append(requestHeaders.get(key)).append("\n");
        }

        return "RequestHeaders{" +
                "\n requestHeaders=" + sb +
                '}';
    }
}
