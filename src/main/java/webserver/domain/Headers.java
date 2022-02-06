package webserver.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.HttpRequestUtils.Pair;

public class Headers {

    private final static String INVALID_ACCEPT_VALUE_MESSAGE = "올바르지 않은 accept 값입니다.";

    private final static String HEADER_FORMAT = "%s: %s\r\n";
    private final static String HEADER_VALUE_SEPARATOR = ",";
    private final static String CONTENT_TYPE = "Content-Type";
    private final static String CONTENT_LENGTH = "Content-Length";
    private final static String CHARSET_UTF8 = "; charset=utf-8";
    private final static String LOCATION = "Location";

    private final Map<String, String> headers;

    private Headers(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    public Headers(Headers headers) {
        this(headers.headers);
    }

    public static Headers createRequestHeader(List<Pair> headerPairs) {
        Map<String, String> headers = new HashMap<>();
        for (Pair headerPair : headerPairs) {
            headers.put(headerPair.getKey(), headerPair.getValue());
        }
        return new Headers(headers);
    }

    public static Headers createResponseHeader(int lengthOfBody, String contentTypes) {
        Map<String, String> headers = new HashMap<>();
        if (contentTypes != null) {
            createHeaders(lengthOfBody, contentTypes, headers);
        }
        return new Headers(headers);
    }

    public static Headers createResponseHeader(String redirect) {
        Map<String, String> headers = new HashMap<>();
        headers.put(LOCATION, redirect);
        return new Headers(headers);
    }

    private static void createHeaders(int lengthOfBody, String contentTypes, Map<String, String> headers) {
        headers.put(CONTENT_LENGTH, Integer.toString(lengthOfBody));
        String[] splitedContentType = contentTypes.split(HEADER_VALUE_SEPARATOR);
        if (splitedContentType.length == 0) {
            throw new IllegalArgumentException(INVALID_ACCEPT_VALUE_MESSAGE);
        }
        headers.put(CONTENT_TYPE, splitedContentType[0] + CHARSET_UTF8);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            sb.append(String.format(HEADER_FORMAT, key, headers.get(key)));
        }
        return sb.toString();
    }

    public String getHeaderAttribute(String key) {
        return headers.get(key);
    }
}
