package model;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class RequestHeader {
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private Map<String, String> cookies;

    public RequestHeader() {
        headers = new HashMap<>();
        parameters = new HashMap<>();
    }

    public String getAccept() {
        return headers.get("Accept").split(",")[0];
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void putParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void setCookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }
}
