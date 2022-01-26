package webserver.http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaders {

    private final Map<String, String> headers = new HashMap<>();

    void addHeader(String httpRequestHeaderKey, String httpRequestHeaderValue) {
        headers.put(httpRequestHeaderKey, httpRequestHeaderValue);
    }

    public int getSize() {
        return headers.size();
    }
}
