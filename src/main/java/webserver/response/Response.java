package webserver.response;

import webserver.header.Cookie;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private final String path;
    private final List<Cookie> cookies;

    public Response(String path) {
        this.path = path;
        this.cookies = new ArrayList<>();
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public String getPath() {
        return path;
    }

    public String getCookie() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).toHeader());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
