package servlet.view;

import http.header.Cookie;
import http.message.ResponseMessage;
import http.startline.HttpStatus;

public class RedirectView implements View {
    private final String url;
    private final Cookie cookie;

    public RedirectView(String url, Cookie cookie) {
        this.url = url;
        this.cookie = cookie;
    }

    @Override
    public ResponseMessage render() {
        if (cookie != null) {
            return ResponseMessage.create(HttpStatus.FOUND, url, cookie);
        }
        return ResponseMessage.create(HttpStatus.FOUND, url);
    }
}
