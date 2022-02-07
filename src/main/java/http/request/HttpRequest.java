package http.request;

import http.Cookie;
import http.HttpHeaders;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private HttpRequestLine requestLine;
    private HttpHeaders requestHeaders;
    private String body;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpHeaders httpRequestHeader, String body) throws IOException {
        requestLine = httpRequestLine;
        requestHeaders = httpRequestHeader;
        this.body = body;
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getParam(String paramName) {
        return requestLine.getParam(paramName);
    }

    public Optional<String> getHeader(String headerName) {
        return requestHeaders.getHeader(headerName);
    }

    public boolean isMethod(HttpMethod method) {
        return requestLine.isMethod(method);
    }

    public String getBody() {
        return body;
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public Optional<Cookie> getCookie() {
        return getHeader("Cookie").map(Cookie::new);
    }
}
