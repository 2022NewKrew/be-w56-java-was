package webserver.model;

import java.util.Optional;

public class HttpResponse {
    private static final String PREFIX_OF_REDIRECTION = "redirect:";

    private final String viewPath;
    private final Cookies cookies;
    private final HttpStatus httpStatus;

    public HttpResponse(String viewPath) {
        this.viewPath = viewPath;
        this.cookies = new Cookies();
        this.httpStatus = HttpStatus.OK;
    }

    public HttpResponse(String viewPath, HttpStatus httpStatus) {
        this.viewPath = viewPath;
        this.cookies = new Cookies();
        this.httpStatus = httpStatus;
    }

    public void addCookie(String name, String value) {
        cookies.addCookie(name, value);
    }

    public Optional<Cookie> getCookieByName(String name) {
        return cookies.getCookieByName(name);
    }

    public String getHttpHeaderOfSetCookie() {
        return String.format("Set-Cookie: %s\r\n", cookies.getHttpHeader());
    }

    public boolean isRedirect() {
        return viewPath.startsWith(PREFIX_OF_REDIRECTION);
    }

    public String getRedirectUrl() {
        return viewPath.substring(PREFIX_OF_REDIRECTION.length());
    }

    public String getViewPath() {
        return viewPath;
    }

    public boolean hasCookies() {
        return !cookies.isEmpty();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
