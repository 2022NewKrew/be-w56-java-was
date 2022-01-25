package webserver.http.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import webserver.http.response.HttpResponse.Header;

public class HttpResponseHeaders {

    private final Map<Header, String> headers = new HashMap<>();

    public Map<Header, String> getHeaders() {
        return headers;
    }

    void addHeader(Header httpResponseHeaderKey, String httpResponseHeaderValue) {
        headers.put(httpResponseHeaderKey, httpResponseHeaderValue);
    }

    public Set<Header> keySet() {
        return headers.keySet();
    }

    public String getValue(Header header) {
        return headers.get(header);
    }
}
