package webserver.request;

import java.util.Collections;
import java.util.Map;

public class RequestHeader {
    private final Map<String, String> headers;

    public RequestHeader(Map<String, String> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "headers=" + headers +
                '}';
    }
}
