package webserver.request;

import java.util.Collections;
import java.util.Map;

public class HttpRequestHeader {
    private Map<String, String[]> headers;

    public HttpRequestHeader(Map<String, String[]> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }
}
