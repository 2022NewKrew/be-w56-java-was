package webserver.model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
    private String protocol;
    private String method;
    private String requestURI;
    private Map<String, String> headers;

    public HttpRequestHeader(String method, String requestURI, String protocol) {
        this.method = method;
        this.requestURI = requestURI;
        this.protocol = protocol;
        this.headers = new HashMap<>();
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getContentType() {
        return headers.get("Accept").split(",")[0];
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

}
