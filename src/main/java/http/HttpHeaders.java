package http;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class HttpHeaders {

    public static final String DELIMITER = ": ";

    public static final String ACCEPT = "Accept";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_TYPE = "Content-Type";

    private final Map<String, String> headers;

    private HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpHeaders of(Map<String, String> headerMap) {
        Objects.requireNonNull(headerMap);
        return new HttpHeaders(headerMap);
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public String getAccept() {
        return headers.get(ACCEPT);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(DELIMITER).append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }
}
