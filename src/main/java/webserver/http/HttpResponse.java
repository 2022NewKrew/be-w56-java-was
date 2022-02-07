package webserver.http;

import java.io.OutputStream;
import java.util.Optional;

public class HttpResponse {
    private static final String NEW_LINE = "\r\n";

    private final OutputStream out;
    private final HttpHeaders headers;
    private final Cookies cookies;

    public HttpResponse(OutputStream out) {
        this.out = out;
        this.headers = new HttpHeaders();
        this.cookies = new Cookies();
    }

    public void addCookie(String name, String value) {
        cookies.addCookie(name, value);
    }

    public Optional<Cookie> getCookieByName(String name) {
        return cookies.getCookieByName(name);
    }

    public boolean hasCookies() {
        return !cookies.isEmpty();
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public void setContentType(String contentType) {
        headers.addHeaders("Content-Type", String.format("%s;charset=utf-8", contentType));
    }

    public String getStringOfHeaders() {
        if (hasCookies()) {
            headers.addHeaders("Set-Cookie", cookies.getHttpHeader());
        }
        return headers.join(NEW_LINE) + NEW_LINE + NEW_LINE;
    }

    public void setLocation(String location) {
        headers.addHeaders("Location", location);
    }

}
