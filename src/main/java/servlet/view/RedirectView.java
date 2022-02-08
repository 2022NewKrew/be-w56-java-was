package servlet.view;

import http.header.Cookie;
import http.message.ResponseMessage;
import http.startline.HttpStatus;

public class RedirectView implements View {
    private final String path;

    public RedirectView(String path) {
        this.path = "http://localhost:8080" + path;
    }

    public ResponseMessage render(Model model, Cookie cookie) {
        return ResponseMessage.create(HttpStatus.FOUND, path, cookie);
    }
}
