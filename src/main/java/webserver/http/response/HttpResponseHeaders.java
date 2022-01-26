package webserver.http.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import webserver.http.response.HttpResponseHeader;

public class HttpResponseHeaders {

    private final Map<HttpResponseHeader, String> headers = new HashMap<>();

    public Map<HttpResponseHeader, String> getHeaders() {
        return headers;
    }

    void addHeader(HttpResponseHeader httpResponseHeaderKey, String httpResponseHeaderValue) {
        headers.put(httpResponseHeaderKey, httpResponseHeaderValue);
    }

    public Set<HttpResponseHeader> keySet() {
        return headers.keySet();
    }

    public String getValue(HttpResponseHeader header) {
        return headers.get(header);
    }
}
