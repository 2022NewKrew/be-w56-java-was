package webserver;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookies) {
        this.cookies = HttpRequestUtils.parseCookies(cookies);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }
}
