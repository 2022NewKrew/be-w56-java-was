package webserver.http;

import http.HttpHeaders;
import http.StatusCode;

import java.util.List;

public class ResponseEntity<T> {
    private HttpHeaders httpHeaders;
    private List<Cookie> cookies;
    private StatusCode statusCode;
    private String view;

    public ResponseEntity(HttpHeaders headers, List<Cookie> cookies, StatusCode statusCode, String view) {
        this.httpHeaders = headers;
        this.cookies = cookies;
        this.statusCode = statusCode;
        this.view = view;
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

    public String getView() {
        return view;
    }
}
