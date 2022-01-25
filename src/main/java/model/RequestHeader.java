package model;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private String method = "";
    private String uri = "";
    private String protocol = "";
    private final Map<String, String> headers = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAccept() {
        return headers.get("Accept").split(",")[0];
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }
}
