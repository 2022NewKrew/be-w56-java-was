package domain;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeaders headers;
    private HttpBody body;

    public HttpRequest(RequestLine requestLine, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.headers = new HttpHeaders(headers);
    }

    public void addHttpBody(Map<String, String> body) {
        this.body = new HttpBody(body);
    }

    public String getRequestPath() {
        return requestLine.getPath();
    }

    public HttpBody getHttpBody() {
        return body;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getValueByHeader("Content-Length"));
    }

    public String getCookie(String cookieName) {
        return HttpRequestUtils.parseCookies(headers.getValueByHeader("Cookie")).get(cookieName);
    }
}
