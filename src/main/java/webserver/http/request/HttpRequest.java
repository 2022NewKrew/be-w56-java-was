package webserver.http.request;

import webserver.http.domain.Cookie;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String requestUri;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> params = null;
    private Cookie cookie;

    protected HttpRequest() {}

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public String getHeaderValue(String k) {
        return header.get(k);
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(String value) {
        this.cookie = new Cookie(value);
    }

    public void setCookie() {
        this.cookie = new Cookie(header.get("Cookie"));
    }
}
