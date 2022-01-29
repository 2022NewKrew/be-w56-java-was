package webserver.http;

import http.HttpHeaders;
import http.StatusCode;
import webserver.http.response.ResponseBody;

import java.util.List;

public class ResponseEntity<T extends ResponseBody<?>> {
    private HttpHeaders httpHeaders;
    private List<Cookie> cookies;
    private StatusCode statusCode;
    private T responseBody;

    public ResponseEntity(HttpHeaders headers, List<Cookie> cookies, StatusCode statusCode, T responseBody) {
        this.httpHeaders = headers;
        this.cookies = cookies;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeaders(String headerName, String headerValue) {
        this.httpHeaders.addHeader(headerName, headerValue);
    }
}
