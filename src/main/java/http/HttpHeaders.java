package http;

import java.util.Map;
import util.HttpRequestUtils;

public class HttpHeaders {

    private static final String HEADER_KEY_VALUE_DELIMITER = ": ";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String COOKIE = "Cookie";

    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, "0"));
    }

    public MediaType getContentType() {
        return MediaType.matchType(headers.getOrDefault(CONTENT_TYPE, "*/*"));
    }

    public Map<String, String> getCookie() {
        return HttpRequestUtils.parseCookies(headers.get(COOKIE));
    }

    public String getKeyValueString() {
        StringBuilder sb = new StringBuilder();

        headers.forEach(
                (key, value) -> sb.append(key)
                        .append(HEADER_KEY_VALUE_DELIMITER)
                        .append(value)
                        .append(System.lineSeparator()));

        return sb.toString();
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public String get(String key) {
        return headers.get(key);
    }
}
