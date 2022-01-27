package webserver.http.request;

import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> query;

    public HttpRequestHeader(Map<String, String> query) {
        this.query = query;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
