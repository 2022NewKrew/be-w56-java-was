package webserver.core.http.request;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String url;
    private final Header header;
    private final Map<String, String> params;
    private final Map<String, String> body;

    public HttpRequest(String url, HttpMethod method, Header header, Map<String, String> params, Map<String, String> body) {
        this.method = method;
        this.url = url;
        this.header = header;
        this.params = params;
        this.body = body;
    }

    public String getUrl() {
        return this.url;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public Header getHeader() {
        return this.header;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public Map<String, String> getBody() {
        return this.body;
    }

}

