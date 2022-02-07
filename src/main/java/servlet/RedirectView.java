package servlet;

import http.Cookie;
import http.HttpStatus;
import http.ResponseMessage;

public class RedirectView implements View {
    private final String url;
    private final Cookie cookie;

    public RedirectView(String url, Cookie cookie) {
        this.url = url;
        this.cookie = cookie;
    }

    @Override
    public ResponseMessage render() {
        return ResponseMessage.create(HttpStatus.FOUND, url, cookie);
    }
}
