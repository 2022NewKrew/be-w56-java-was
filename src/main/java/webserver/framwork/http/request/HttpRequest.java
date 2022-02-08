package webserver.framwork.http.request;

import webserver.framwork.http.Header;

import java.util.Arrays;
import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String url;
    private Header header;
    private Map<String, String> params;
    private Map<String, String> body;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getBody() {
        return this.body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public String getCookie(String key) {
        return this.header.getCookie(key);
    }
}

