package webserver.cookie;

import webserver.util.HttpRequestUtils;

import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookieValue) {
        this.cookies = HttpRequestUtils.parseCookies(cookieValue);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }
}
