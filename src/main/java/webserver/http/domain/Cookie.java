package webserver.http.domain;

import webserver.http.request.HttpRequestUtils;

import java.util.Map;

public class Cookie {
    private Map<String, String> cookie;

    public Cookie(String value) {
        this.cookie = HttpRequestUtils.parseCookies(value);
    }

    public String getCookie(String k) {
        return cookie.get(k);
    }
}
