package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    public String getHeaderByName(String headerName) {
        return headers.get(headerName);
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public Map<String, String> getAllHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
