package http.cookie;

import http.header.HttpHeaders;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpCookie {
    private final Map<String, String> cookies;

    public HttpCookie() {
        this.cookies = new HashMap<>();
    }

    public HttpCookie(HttpHeaders headers) {
        this();
        List<String> cookieString = headers.get(HttpHeaders.COOKIE);
        if (cookieString != null) {
            cookieString.forEach(cookie -> {
                String[] parsedCookie = HttpRequestUtils.parseCookies(cookie);
                add(parsedCookie[0], parsedCookie[1]);
            });
        }
    }

    public void add(String key, String value) {
        cookies.put(key, value);
    }

    public String get(String key) {
        return cookies.get(key);
    }
}
