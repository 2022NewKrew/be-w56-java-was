package model;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private String method;
    private String path;
    private final Map<String, String> headers;

    public RequestHeader() {
        this.method = "";
        this.path = "";
        this.headers = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return headers.get("Accept").split(",")[0];
    }
}
