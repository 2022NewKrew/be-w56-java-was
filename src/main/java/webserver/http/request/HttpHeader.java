package webserver.http.request;

import java.util.Map;

public class HttpHeader {

    private final Map<String, String> headerMap;
    private final Map<String, String> cookieMap;

    public HttpHeader(Map<String, String> headerMap, Map<String, String> cookieMap) {
        this.headerMap = headerMap;
        this.cookieMap = cookieMap;
    }

    public String getHeaderAttribute(String key) {
        return headerMap.get(key);
    }

    public String getCookie(String key) {
        return cookieMap.get(key);
    }

    public void setPath(String path) {
        this.headerMap.put("PATH", path);
    }
}
