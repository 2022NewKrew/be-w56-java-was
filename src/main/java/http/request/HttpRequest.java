package http.request;

import http.cookie.HttpCookie;
import http.header.HttpHeaders;

public class HttpRequest {
    private HttpRequestLine requestLine;
    private HttpHeaders headers;
    private HttpRequestBody body;
    private HttpCookie cookie;

    public HttpRequest(HttpRequestLine requestLine, HttpHeaders requestHeader, HttpRequestBody requestBody) {
        this.requestLine = requestLine;
        this.headers = requestHeader;
        this.body = requestBody;
        this.cookie = new HttpCookie(headers);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getUrl() {
        return requestLine.getUrl();
    }

    public String getHeader(String header) {
        return headers.getFirst(header);
    }

    public String getParam(String param) {
        return requestLine.containsParam(param) ? requestLine.getParam(param) : body.getParam(param);
    }

    public String getCookie(String key) {
        return cookie.get(key);
    }
}
