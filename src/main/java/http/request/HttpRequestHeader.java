package http.request;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> header;

    public HttpRequestHeader(Map<String, String> header) {
        this.header = header;
    }

    public static HttpRequestHeader parseQueryString(String queryString) {
        return new HttpRequestHeader(HttpRequestUtils.parseQueryString(queryString));
    }
}
