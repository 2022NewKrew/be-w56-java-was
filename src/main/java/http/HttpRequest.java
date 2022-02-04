package http;

import enums.HttpMethod;
import enums.HttpProtocol;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private HttpMethod method;
    private String url;
    private String queryString;
    private HttpProtocol protocol;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> body = new HashMap<>();

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setStartLine(String method, String url, String protocol) {
        setMethod(method);
        setUrl(url);
        setProtocol(protocol);
    }

    private void setMethod(String method) {
        this.method = HttpMethod.valueOf(method);
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    private void setProtocol(String protocol) {
        for (HttpProtocol p : HttpProtocol.values()) {
            if (p.getName().equals(protocol)) {
                this.protocol = p;
            }
        }
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", queryString='" + queryString + '\'' +
                ", protocol=" + protocol +
                ", body=" + body +
                '}';
    }
}
